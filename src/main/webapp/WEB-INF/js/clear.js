function clearForm() {

    document.getElementsByName("filterForm")[0].value = null;
    document.getElementsByName("filterForm")[1].value = null;
    document.getElementsByName("filterForm")[2].value = null;
    document.getElementsByName("filterForm")[3].value = null;

    document.getElementById("1").value = null;
    document.getElementById("2").value = null;

    // filterForm.fromDate.value = null;
    // filterForm.toDate.value = null;
    // filterForm.fromTime.value = null;
    // filterForm.toTime.value = null;
}
