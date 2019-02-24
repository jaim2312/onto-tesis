var Cargo = function () {

    return {
    	load_botones: function () {
    		$("#btn_new").click(function(event){
    			event.preventDefault();
    			
    			$("#txt_id").val(0);
    			$("#txt_nombre").val("");
    		});
    		
    		$("#btn_save").click(function(event){
    			event.preventDefault();
    			
    			var id = $("#txt_id").val();
    			var nombre = $("#txt_nombre").val();
    			
            	$.ajax({
                    url: root + "cargo/guardar",
                    type: "POST",
                    data: {
                    	id: id,
                    	nombre: nombre
                    },
                    success: function () {
                    	$("#dt_cargo").dataTable()._fnAjaxUpdate();
                    }
                });
    		});
        },
        load_datatable: function () {
	        //inicializo el plugin datatable sobre la tabla
	        $("#dt_cargo").dataTable({
                //"language": {
                //    "url": root + "application/assets/bower_components/datatables.net/spanish.json"
                //},
	            "ajax": {
                    "url": root + "cargo/dt_QueCargo",
                    "type": "POST",
                    "data": function (p) {
                        //p.contextid = $("#hdn_select_contextid").val();
                    }
                },
	            "filter": false,
                "processing": true,
                "serverSide": true,                
	            "ordering": false,
	            "pageLength": 10,
	            "lengthChange": false,
                "autoWidth": false,
                "columnDefs": [
    				{ "width": "80%", "targets": 0 },
    				{ "width": "20%", "targets": 1 }
  				]
	        });
        },
        edit_cargo: function(id){
        	$.ajax({
                url: root + "cargo/getCargo",
                type: "POST",
                data: {
                	id: id
                },
                success: function (data) {
                	console.log(data);
                	$("#txt_id").val(data.nId);
                	$("#txt_nombre").val(data.sNombre);
                }
            });
        },
        add_conocimiento: function(id){
        	
        }
    }

} ();


$(document).ready(function () {
	Cargo.load_botones();
	Cargo.load_datatable();
});