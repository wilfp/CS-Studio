
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

var codeMirror;
var app;

var infoText;
var infoPanel;
var textIndex = 0;

function onLoad(){
    
    infoPanel = $("#info-desc");
    infoText = infoPanel.text();
    infoPanel.text("");
    infoPanel.removeClass("hidden");
    displayInfoText();
    
    var has_indent = document.getElementById("has-indent") == "true";
    
    codeMirror = CodeMirror.fromTextArea(document.getElementById("code-area"), {
    lineNumbers: true,
    matchBrackets: true,
    mode: "text/x-java",
    theme: "eclipse",
    smartIndent: has_indent
  });
  
  codeMirror.setSize(null, 500);
  
  codeMirror.on("change", function(){ codeChanged = true; });
    
    Vue.component('output-panel', {
      
      props: [ "className", "content" ],
      
      template: '<div :class="className"> {{ content }} </div>'
      
    });
        
   app = new Vue({
	  delimiters: ['[[', ']]'],
	  el: '#code-output',
      data:{
          outputs: []
      }
	});
        
}

function displayInfoText(){
    
    if(textIndex <= infoText.length){
        var nextChar = infoText.charAt(textIndex);
        infoPanel.text(infoText.substring(0, textIndex));
        textIndex++;
        setTimeout(displayInfoText, 30);
    }
}

// Called when the run button is clicked
function runCodeFunction() {
    
    // If no changes
    if(!codeChanged){
        startLineAnimation();
        return;
    }
    
	var codeArea = codeMirror.getValue();
	codeArea = window.btoa(codeArea);
	
	var result = sendPost(codeArea);
}

function getChallengeID(){
	return $("#challengeID").html();
}

function setLoading(status){
    
    if(status){
        $("#button-play-code").addClass("is-loading");
    }else{
        $("#button-play-code").removeClass("is-loading");
    }
}

function sendPost(code) {
	
	setLoading(true);
	
	var challengeID = getChallengeID();
	var jsonData = '{ \"challenge_id\": \"' + challengeID  + '\", \"code\": \"' + code + '\" }';
	
    $.ajax({
        type: "POST",
        url: '/learn/runJavaCode',
		dataType: 'json',
        data: jsonData,
        success: response
    });

}

function response(msg, status, jqXHR) {    
	
	app.$data.outputs.push( { content: msg["text"], className: getStatus(msg["status"]) } );
    
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
function startLineAnimation(){
    shouldRun = true;
    currentLine = 0;
    highlightNextLine();
}

// Called recursively to highlight each line in turn
function highlightNextLine(){
    
    // Highlight current line
    highlightLine(lines[currentLine]);
    // Get next line
    currentLine++;
    
    // If lines left and not stopped
    if(currentLine < lines.length && shouldRun){
        // Schedule next line
        setTimeout(highlightNextLine, 250);
    }else{
        // Clear last line
        lastMarker.clear();
    }
}

function getStatus(status){
    
    if(status == "SUCCESS"){
        return "notification is-info";
    }
    
    return "notification is-danger";
}

// The last line marked, used to clear the marked area
var lastMarker = null;

// Highlights one line of the code area
function highlightLine(x){
    
    if(lastMarker != null){
        lastMarker.clear();
    }
    
    lastMarker = codeMirror.markText({line: x, ch: 0}, {line: x+1, ch: 0}, {className: "highlight"});
}

$(document).ready(function(){
    
    $("#button-play-code").click(runCodeFunction);
    
	onLoad();
});
