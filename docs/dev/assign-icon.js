function assignIcon() {
    var types = document.querySelector('div.type-signature span.modifiers')?.textContent;
    var eI = document.querySelector('span.extends-implements')?.textContent.split(/\n/);

    var head = document.head;

    var file = (function () {
        if (types === undefined) return 'icon';
        if (types.includes('interface')) return 'interface';
        else if (types.includes('class') && eI[0].match(/extends \w+Exception/)) return 'exception'
        else if (types.includes('abstract')) return 'abstract-class';
        else if (types.includes('final')) return 'final-class';
        else if (types.includes('record')) return 'record';
        else return 'icon';
    })();

    var iconRef = document.createElement('link');
    iconRef.rel = 'shortcut icon'
    iconRef.type = 'image/x-icon'
    iconRef.href =  pathtoroot + 'resources/' + file + '.ico';

    head.appendChild(iconRef);
}

if (document.readyState !== 'loading') assignIcon();
else document.addEventListener("DOMContentLoaded",  assignIcon);