
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

function onLoad(){
    
    var has_indent = document.getElementById("has-indent") == "true";
    
    codeMirror = CodeMirror.fromTextArea(document.getElementById("code-area"), {
    lineNumbers: true,
    matchBrackets: true,
    mode: "text/x-java",
    smartIndent: has_indent
  });
  
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

function runCodeFunction() {
		
	var codeArea = codeMirror.getValue();
	codeArea = window.btoa(codeArea);
	
	var result = sendPost(codeArea);
}

function getChallengeID(){
	return $("#challengeID").html();
}

function setProgress(progress){
	$("#code-progress").html(function(){
		return progress;
	});
}

function sendPost(code) {
	
	setProgress('<progress class="progress is-small is-info" max="25"></progress>');
	
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
    currentLine = 0;
    highlightNextLine();
    
	setProgress('');
}

// The current lines being displayed
var lines = [];
var currentLine = 0;

// Called recursively to highlight each line in turn
function highlightNextLine(){
    
    // Highlight current line
    highlightLine(lines[currentLine] - 1);
    // Get next line
    currentLine++;
    
    // If lines left
    if(currentLine < lines.length){
        // Schedule next line
        setTimeout(highlightNextLine, 350);
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
		
    $("#runcode").click(runCodeFunction);
	onLoad();
});
