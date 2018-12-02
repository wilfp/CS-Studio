
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
	
	addOutput("Testing<br>Testing1");
	addOutput("Testing<br>Testing1", "error");
	addOutput("Testing<br>Testing1", "success");
}

$(document).ready(function(){
    $("#runcode").click(runCodeFunction);
});