function validateForm() {
    if( !$('#new_wallet').val() ){
        alert("Please select value.")
        return false;
    }
}

function validateCombo() {
    var combo1 = document.getElementById("money_container")
    if (combo1.value == null || combo1.value == "") {
        alert("Please select a value");
        return false;
    }
}