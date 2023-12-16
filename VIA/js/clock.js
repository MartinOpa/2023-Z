function updateTime() {
    const date = document.getElementById("date");
    const time = document.getElementById("time");
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    date.textContent = `${day}.${month}.${year}`;
    time.textContent = `${hours}:${minutes}:${seconds}`;
}

function initClock() {
    updateTime();
    setInterval(updateTime, 1000);
}

