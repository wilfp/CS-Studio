
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
	
	var codeArea = $("#codeArea").html();
	
	var data = '{ \"code\": \"${codeArea}\" }';
	
	var result = sendPost(data);
}

function sendPost(data) {
    $.ajax({
        type: "POST",
        url: '/learn/runJavaCode',
        json: data,
        success: success
    });
}

function success(result) {
    alert('Process achieved!');
	addOutput(result["text"], result["status"]);
}

$(document).ready(function(){
    $("#runcode").click(runCodeFunction);
});