$(function() {

var input = $("#input");
var output = $("#output");
input.on('keyup change', function(event) {
	var inputValue = event.target.value;
	var result = inputValue * 2;
	$("#output").html(result);
});

});