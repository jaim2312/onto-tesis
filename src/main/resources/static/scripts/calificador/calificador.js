var Calificador = function () {

    return {
    	load_botones: function () {	
    		$("#btn_procesar").click(function(event){
    			event.preventDefault();    		
    			
    			var cargoid = $("#hdn_cargoid").val();
    			
            	$.ajax({
                    url: root + "calificador/procesarcv",
                    type: "POST",
                    data: {
                    	cargoid: cargoid
                    },
                    success: function (data) {
                    	console.log(data);
                    	//$("#modal_process_cv").modal({backdrop: 'static', keyboard: false}); 
                    	//alert(data);
                    	var html = "";
                    	
                    	$.each(data, function (index, value) {
                    		html += "<tr>";
                    		html += "<td>"+(index+1)+"</td>";
                    		html += "<td>"+value.fileNameCV+"</td>";
                    		html += "<td>"+value.arrIndividuoEnc.length+"</td>";
                    		
                    		var skills = "";
                    		
                    		$.each(value.arrIndividuoEnc, function (i, skill) {
                    			if(i == value.arrIndividuoEnc.length - 1) skills += skill.descOnto;
                    			else skills += skill.descOnto+" ,";
                    		});
                    		
                    		html += "<td>"+skills+"</td>";
                    		//console.log(value.fileNameCV);
                    		html += "</tr>";
                    	});
                    	
                    	$("#tbl_result_cv tbody").html(html);
                    	$("#modal_process_cv").modal({backdrop: 'static', keyboard: false});                  	
                    	
                    }
                });
    		});
        }
    }

} ();


$(document).ready(function () {	
	Calificador.load_botones();
});