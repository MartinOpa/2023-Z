function updateCountdown() {
    const now = new Date();
    
    const tYear = parseInt(document.getElementById('cYear').value);
    const tMonth = parseInt(document.getElementById('cMonth').value) - 1; 
    const tDay = parseInt(document.getElementById('cDay').value);
    const tHour = parseInt(document.getElementById('cHour').value);
    const tMinute = parseInt(document.getElementById('cMinute').value);
    const tSecond = parseInt(document.getElementById('cSecond').value);
    const timeDif = new Date(tYear, tMonth, tDay, tHour, tMinute, tSecond) - new Date();
    
    var years = Math.floor(timeDif / (365.25 * 24 * 60 * 60 * 1000));
    if (years <= 0) {
        years = 0;
    }
    var months = Math.floor((timeDif % (365.25 * 24 * 60 * 60 * 1000)) / (30.44 * 24 * 60 * 60 * 1000));
    if (months <= 0) {
        months = 0;
    }
    var days = Math.floor((timeDif % (30.44 * 24 * 60 * 60 * 1000)) / (24 * 60 * 60 * 1000));
    if (days <= 0) {
        days = 0;
    }
    var hours = Math.floor((timeDif % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000));
    if (hours <= 0) {
        hours = 0;
    }
    var minutes = Math.floor((timeDif % (60 * 60 * 1000)) / (60 * 1000));
    if (minutes <= 0) {
        minutes = 0;
    }
    var seconds = Math.floor((timeDif % (60 * 1000)) / 1000);
    if (seconds <= 0) {
        seconds = 0;
    }

    document.getElementById('countdownTextElementDate').textContent = `${years} let, ${months} měsíců, ${days} dní`;
    document.getElementById('countdownTextElementTime').textContent = `${hours}:${minutes}:${seconds}`;

    document.getElementById('eventNameHeader').textContent = document.getElementById('cEventNameHeader').value;
}

function loadCountdown() {
    if (localStorage.getItem('cYear') == undefined || localStorage.getItem('cYear') == null) {
        resetCountdown();
    } else {
        document.getElementById('cYear').value = localStorage.getItem('cYear');
        document.getElementById('cMonth').value = localStorage.getItem('cMonth');
        document.getElementById('cDay').value = localStorage.getItem('cDay');
        document.getElementById('cHour').value = localStorage.getItem('cHour');
        document.getElementById('cMinute').value = localStorage.getItem('cMinute');
        document.getElementById('cSecond').value = localStorage.getItem('cSecond');  
        document.getElementById('cEventNameHeader').value = localStorage.getItem('cEventNameHeader')
    }
}

function initCountdown() {
    loadCountdown();
    updateCountdown();
    toggleSettings();
    setInterval(updateCountdown, 1000);
}

function resetCountdown() {
    const now = new Date();
    document.getElementById('cYear').value = now.getFullYear();
    document.getElementById('cMonth').value = now.getMonth() + 1;
    document.getElementById('cDay').value = now.getDate();
    document.getElementById('cHour').value = now.getHours();
    document.getElementById('cMinute').value = now.getMinutes();
    document.getElementById('cSecond').value = now.getSeconds();
    document.getElementById('cEventNameHeader').value = 'Počítadlo';
    saveCountdown();
}

function saveCountdown() {
    localStorage.setItem('cYear', document.getElementById('cYear').value);
    localStorage.setItem('cMonth', document.getElementById('cMonth').value) - 1;
    localStorage.setItem('cDay', document.getElementById('cDay').value);
    localStorage.setItem('cHour', document.getElementById('cHour').value);
    localStorage.setItem('cMinute', document.getElementById('cMinute').value);
    localStorage.setItem('cSecond', document.getElementById('cSecond').value);
    localStorage.setItem('cEventNameHeader', document.getElementById('cEventNameHeader').value);
}

function toggleSettings() {
    const settings = document.querySelector('.collapsible');
    settings.style.display = settings.style.display === 'none' ? 'block' : 'none';
    document.querySelector('.arrow').classList.toggle('flip');
}

