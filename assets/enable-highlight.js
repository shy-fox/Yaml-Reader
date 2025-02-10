var kw = /(final|static|abstract|native|char|byte|short|int|long|double|float|boolean|null|void|public|private|protected)/;
var an = /(@[^ ]+)/;

function applySyntax() {
    var methods = document.querySelectorAll('section.method-summary div[class^=col-]');

    methods.forEach(function(c, i) {
        if (i % 3 != 2) {
            // keywords
            c.innerHTML = c.innerHTML.replace(kw, "<kw>$1</kw>")
            // annotations
            c.innerHTML = c.innerHTML.replace(an, "<an>$1</an>")
        }
    });
}

if (document.readyState !== 'loading') applySyntax();
else document.addEventListener("DOMContentLoaded",  applySyntax);