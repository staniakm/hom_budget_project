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

var amountInput = document.getElementById(description);

function validateFormAmount() {
    //Get the trimmed name
    var amount = trim(amountInput.value)

    if(amount) {
        //Update the form with the trimmed value, just before the form is sent
        amountInput.value = amount
    }
    else {
        //Trimmed value is empty
        alert("Animal should have a name");
        amountInput.focus();
        return false;
    }
}

function trim(value) {
    return value.replace(/^\s+|\s+$/g,"");
}