// function to change bg:
window.onload = () => {
    let mode = 'dark-mode'
    const head = document.head;
    const modeStyle = document.head.querySelector('link.colors');

    const updateMode = () => {
        modeStyle.href = `assets/css/colors-${mode}.css`;
    }

    updateMode();

    // sidebar handling
    const menuHeight = 40;
    const menuBtnHeight = 5;

    const sidebar = document.body.querySelector('div.sidebar');
    const sidebarMenu = sidebar.querySelector('div.menu');
    const modeButton = sidebar.querySelector('div.mode');
    const colorIcon = modeButton.querySelector('i');

    sidebarMenu.onclick = () => {
        var menuVis = sidebarMenu.getAttribute('aria-label');
        sidebarMenu.setAttribute('aria-label', (menuVis == 'shown' ? 'hidden' : 'shown'));
        sidebar.setAttribute('aria-label', (menuVis == 'shown' ? 'shown' : 'hidden'))
    }

    modeButton.onclick = () => {
        mode = (mode === 'dark-mode' ? 'light-mode' : 'dark-mode');
        colorIcon.className = "fa-regular " + (mode === 'dark-mode' ? 'fa-moon' : 'fa-sun');
        updateMode();
    }
}