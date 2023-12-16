function changeSearch(option) {
    const arrow = document.querySelector('.arrow-up');
    const selectedOption = document.querySelector('.option.active');
    if (selectedOption) {
        const arrowLeft = selectedOption.offsetLeft + selectedOption.offsetWidth / 2;
        arrow.style.left = arrowLeft + 'px';
    }

    const searchForm = document.getElementById('searchForm');

    switch (option) {
        case 'internet':
            searchForm.action = 'https://search.seznam.cz?';  
            return;      
        case 'companies':
            searchForm.action = 'https://www.firmy.cz/?';    
            return;   
        case 'maps':
            searchForm.action = 'https://en.mapy.cz/zakladni?';  
            return;     
        case 'goods':
            searchForm.action = 'https://www.zbozi.cz/hledej/?';  
            return;    
        case 'pics':
            searchForm.action = 'https://search.seznam.cz/obrazky/?'; 
            return;      
    }
}

function handleWindowResize() {
    changeSearch(document.querySelector('.option.active').textContent.trim());
}

document.addEventListener('DOMContentLoaded', function () {
    const options = document.querySelectorAll('.option');
    options.forEach(function (option) {
        option.addEventListener('click', function () {
            options.forEach(function (opt) {
                opt.classList.remove('active');
            });

            option.classList.add('active');
            changeSearch(option.textContent.trim());
        });
    });

    window.addEventListener('resize', handleWindowResize);
});

