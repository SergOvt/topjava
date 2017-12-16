package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    List<Role> roles = new ArrayList<>(user.getRoles());
                    ps.setInt(1, user.getId());
                    ps.setString(2, roles.get(i).name());
                }

                @Override
                public int getBatchSize() {
                    return user.getRoles().size();
                }
            });
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return getWithRoles("SELECT * FROM users u JOIN user_roles r ON u.id=r.user_id WHERE u.id=?", id);
    }

    @Override
    public User getByEmail(String email) {
        return getWithRoles("SELECT * FROM users u JOIN user_roles r ON u.id=r.user_id WHERE email=?", email);
    }

    @Override
    public List<User> getAll() {
        List<User> allUsers = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles",
                rs -> {
                    Map<Integer, Set<Role>> allRoles = new HashMap<>();
                    while (rs.next()) {
                        int id = rs.getInt("user_id");
                        if (allRoles.get(id) == null) {
                            Set<Role> role = new HashSet<>();
                            role.add(Role.valueOf(rs.getString("role")));
                            allRoles.put(id, role);
                        }
                        else {
                            allRoles.get(id).add(Role.valueOf(rs.getString("role")));
                        }
                    }
                    return allRoles;
                });
        allUsers.forEach(u -> u.setRoles(roles.get(u.getId())));
        return allUsers;
    }

    private <K> User getWithRoles(String sql, K key) {
        Map<? extends K, User> users = jdbcTemplate.query(sql,
                rs -> {
                    Map<? extends K, User> user = Collections.singletonMap(key, null);
                    if (rs.next()) {
                        Set<Role> roles = new HashSet<>();
                        roles.add(Role.valueOf(rs.getString("role")));
                        user = Collections.singletonMap(key, new User(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                                rs.getString("password"), rs.getInt("calories_per_day"),
                                rs.getBoolean("enabled"), rs.getTimestamp("registered"), roles));
                    }
                    while(rs.next()) {
                        user.get(key).getRoles().add(Role.valueOf(rs.getString("role")));
                    }
                    return user;
                }, key);
        return users.get(key);
    }
}
