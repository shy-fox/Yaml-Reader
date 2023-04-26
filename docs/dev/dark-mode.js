var light = true;
var cN = "mode-switch";

function addModeSwitch() {
    var lightMode = document.createElement('li');
    var darkMode = document.createElement('li');

    lightMode.className = cN;
    darkMode.className = cN;

    lightMode.textContent = "Light Mode";
    darkMode.textContent = "Dark Mode";

    lightMode.onclick = function() {
        if (light) updateMode();
    }

    darkMode.onclick = function() {
        if (!light) updateMode();
    }

    document.querySelector('ul.nav-list').append(lightMode, darkMode);
}

function updateMode() {
    var modeButtons = document.querySelectorAll('li.' + cN);
    var lightMode = modeButtons[0];
    var darkMode = modeButtons[1];

    if (light) {
        lightMode.className = cN + " highlight";
        darkMode.className = cN;
    }
    else {
         darkMode.className = cN + " highlight";
         lightMode.className = cN;
     }

    if (light) document.body.setAttribute("mode", "light");
    else document.body.setAttribute("mode", "dark");

    light = !light;
}

var load = function () {
    addModeSwitch();
    updateMode();
};

if (document.readyState !== 'loading') load();
else document.addEventListener("DOMContentLoaded",  load);