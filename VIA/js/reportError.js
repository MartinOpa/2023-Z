function validateAndSubmit() {
    var errorEmail = document.getElementById('errorEmail');
    var errorDesc = document.getElementById('errorDesc');
    var errorFullDesc = document.getElementById('errorFullDesc');
    var captcha = document.getElementById('captcha');
    var errorEmailLabel = document.getElementById('errorEmailLabel');
    var errorDescLabel = document.getElementById('errorDescLabel');
    var captchaLabel = document.getElementById('captchaLabel');
    var properLength = true;

    var invalidText = '';
    if (!isValidEmail(errorEmail.value)) {
        invalidText += 'Zadejte, prosím, platnou e-mailovou adresu';
        errorEmailLabel.style.color = 'red';
    } else {
        errorEmailLabel.style.color = 'black';
    }

    if (!isDescProperLength(errorDesc.value)) {
        if (invalidText !== '') {
            invalidText += '\n'
        } 

        invalidText += 'Popis chyby může mít nanejvýš 100 znaků';
        errorDescLabel.style.color = 'red';
        properLength = false;
    } else {
        errorDescLabel.style.color = 'black';
    }

    if (!isDescFilledOut(errorDesc.value)) {
        if (invalidText !== '') {
            invalidText += '\n'
        } 

        invalidText += 'Zadejte, prosím, stručný popis chyby';
        errorDescLabel.style.color = 'red';
    } else if (properLength) {
        errorDescLabel.style.color = 'black';
    }

    if (!isValidCaptcha(captcha.value)) {
        if (invalidText !== '') {
            invalidText += '\n'
        } 
        
        invalidText += 'Nesprávně vyplněná captcha';
        captchaLabel.style.color = 'red';
    } else {
        captchaLabel.style.color = 'black';
    }

    if (invalidText !== "") {
        alert(invalidText);
        return;
    }

    //document.getElementById('errorForm').submit();
    errorEmail.value = '';
    errorDesc.value = '';
    errorFullDesc.value = '';
    captcha.value = '';
    $('#errorForm').modal('hide');
    alert('Děkujeme.');
}

function isValidEmail(errorEmail) {
    var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(errorEmail);
}

function isDescFilledOut(errorDesc) {
    return errorDesc.length > 0;
}

function isDescProperLength(errorDesc) {
    return errorDesc.length <= 100;
}

function isValidCaptcha(captcha) {
    return captcha === 'JmaG';
}

