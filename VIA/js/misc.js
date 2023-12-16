function toggleDropdown() {
    var dropdown = document.getElementById('galleryDropdown');
    dropdown.style.display = 'block';
    dropdown.style.zIndex = '1000';
}

function initDropDown() {
    window.onclick = function (event) {
        if (!event.target.matches('.col-md-1') && !event.target.matches('.row') && !event.target.matches('.art-drop')) {
            var dropdown = document.getElementById('galleryDropdown');
            dropdown.style.display = 'none';
            dropdown.style.zIndex = '0';
        }
    }
}

function navigateTo(url) {
    window.location.href = url;
}

