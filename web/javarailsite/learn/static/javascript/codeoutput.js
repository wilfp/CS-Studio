
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

var codeMirror;

function onLoad(){
	codeMirror = CodeMirror.fromTextArea(document.getElementById("codearea"), {
    lineNumbers: true,
    matchBrackets: true,
    mode: "text/x-java"
  });
}

$.ajaxSetup({
    beforeSend: function(xhr, settings) {
        if (!csrfSafeMethod(settings.type) && !this.crossDomain) {
            xhr.setRequestHeader("X-CSRFToken", csrftoken);
        }
    }
});

function getNotification(output, style){
	return "<div class=\"" + style + "\">" + output + "</div>";
}

function addOutput(output, type="none"){
	
	$("#codeoutput").html(function(i, orginHtml){
		
		var style = null;
		
		if(type == "error"){
			style = "notification is-danger";
		}else if(type == "success"){
			style = "notification is-success";
		}else{
			style = "notification is-link";
		}
		
        return orginHtml + getNotification(output, style);
    });
}

function runCodeFunction() {
	
	var codeArea = codeMirror.getValue();
	
	var result = sendPost(codeArea);
}

function getChallengeID(){
	return $("#challengeID").html();
}

function setProgress(progress){
	$("#codeProgress").html(function(){
		return progress;
	});
}

function sendPost(code) {
	
	setProgress('<progress class="progress is-small is-primary" max="100">15%</progress>');
	
	var challengeID = getChallengeID();
	var jsonData = '{ \"challenge_id\": \"' + challengeID  + '\", \"code\": \"' + code + '\" }';
	
    $.ajax({
        type: "POST",
        url: '/learn/runJavaCode',
		dataType: 'text/json',
        data: jsonData,
        success: response
    });
}

function response(msg, status, jqXHR) {

	alert("response");
	
	json = JSON.parse(msg);
	addOutput("Output: " + json["text"], json["status"]);
	
	setProgress('');
}

$(document).ready(function(){
    $("#runcode").click(runCodeFunction);
	onLoad();
});