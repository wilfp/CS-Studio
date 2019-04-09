function getCookie(name) {
    var cookieValue = null;
    if (document.cookie && document.cookie !== '') {
        var cookies = document.cookie.split(';');
        for (var i = 0; i < cookies.length; i++) {
            var cookie = jQuery.trim(cookies[i]);
            // Does this cookie string begin with the name we want?
            if (cookie.substring(0, name.length + 1) === (name + '=')) {
                cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return cookieValue;
}
var csrftoken = getCookie('csrftoken');

function csrfSafeMethod(method) {
    // these HTTP methods do not require CSRF protection
    return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
}

$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
        }
    }
});

Vue.component('output-panel', {

    props: ["className", "content"],

    template: '<div :class="className"> {{ content }} </div>'

});

Vue.component('text-transition', {

    data: function() {
        return {
            displayTextIndex: 0,
            displayText: null
        }
    },
    template: `
      <transition
      appear
      mode="out-in"
      v-on:appear="onTextAppear"
      v-on:before-appear="onBeforeTextAppear"
      v-bind:css="false"
      >
      <slot></slot>
      </transition>
      `,
    methods: {

        onBeforeTextAppear: function(el) {
            this.displayText = el.innerHTML;
            this.displayTextIndex = 0;
            el.innerHTML = "";
        },

        onTextAppear: function(el, done) {
            displayTextStep(el, this.displayText, this.displayTextIndex);
            done();
        }

    }

});

function displayTextStep(el, displayText, displayTextIndex) {

    if (displayTextIndex <= displayText.length) {
        var nextChar = displayText.charAt(displayTextIndex);
        el.innerHTML += nextChar;
        displayTextIndex++;
        setTimeout(function() {
            displayTextStep(el, displayText, displayTextIndex)
        }, 25);
    }
}

var app = new Vue({
    el: '#app',
    delimiters: ['[[', ']]'],
    data: {
        outputs: []
    }
});

var codeMirror;

function onLoad() {

    var has_indent = document.getElementById("has-indent") == "true";

    codeMirror = CodeMirror.fromTextArea(document.getElementById("code-area"), {
        lineNumbers: true,
        matchBrackets: true,
        mode: "text/x-java",
        theme: "eclipse",
        smartIndent: has_indent
    });

    codeMirror.setSize(null, 500);

    codeMirror.on("change", function() {
        codeChanged = true;
    });
}

// Called when the run button is clicked
function runCodeFunction() {

    // If no changes
    if (!codeChanged) {
        startLineAnimation();
        return;
    }

    var codeArea = codeMirror.getValue();
    codeArea = window.btoa(codeArea);

    var result = sendPost(codeArea);
}

function getChallengeID() {
    return $("#challengeID").html();
}

function setLoading(status) {

    if (status) {
        $("#button-play-code").addClass("is-loading");
    } else {
        $("#button-play-code").removeClass("is-loading");
    }
}

function sendPost(code) {

    setLoading(true);

    var challengeID = getChallengeID();
    var jsonData = '{ \"challenge_id\": \"' + challengeID + '\", \"code\": \"' + code + '\" }';

    $.ajax({
        type: "POST",
        url: '/learn/runJavaCode',
        dataType: 'json',
        data: jsonData,
        success: response
    });

}

function response(msg, status, jqXHR) {

    app.$data.outputs = [];

    var rawText = msg["text"];
    var textLines = rawText.split("\n");
    var statusClass = getStatus(msg["status"]);

    for (var i = 0; i < textLines.length; i++) {
        var line = textLines[i];
        app.$data.outputs.push({
            content: line,
            className: statusClass
        });
    }

    lines = msg["lines"];
    codeChanged = false;
    startLineAnimation();

    setLoading(false);
}

// The current lines being displayed
var lines = [];
var currentLine = 0;
var shouldRun = false;
var codeChanged = true;

// Goes through all lines, highlighting each in turn
function startLineAnimation() {
    shouldRun = true;
    currentLine = 0;
    highlightNextLine();
}

// Called recursively to highlight each line in turn
function highlightNextLine() {

    // Highlight current line
    highlightLine(lines[currentLine]);
    // Get next line
    currentLine++;

    // If lines left and not stopped
    if (currentLine < lines.length && shouldRun) {
        // Schedule next line
        setTimeout(highlightNextLine, 250);
    } else {
        // Clear last line
        lastMarker.clear();
    }
}

function getStatus(status) {

    if (status == "SUCCESS") {
        return "cs-box dark-blue";
    }

    return "cs-box";
}

// The last line marked, used to clear the marked area
var lastMarker = null;

// Highlights one line of the code area
function highlightLine(x) {

    if (lastMarker != null) {
        lastMarker.clear();
    }

    lastMarker = codeMirror.markText({
        line: x,
        ch: 0
    }, {
        line: x + 1,
        ch: 0
    }, {
        className: "highlight"
    });
}

$(document).ready(function() {

    $("#button-play-code").click(runCodeFunction);

    onLoad();
});