var Ontologia = function () {

    return {
    	load_onto_tree: function () {	
            $("#ontoTree")
            .on("deselect_node.jstree", function (e, data) { 
            	console.log(data);
            	$.ajax({
                    url: root + "conocimiento/eliminar",
                    type: "POST",
                    data: {
                    	URI: data.node.a_attr.unique,
                    	cargoId: $("#hdn_cargoid").val()
                    },
                    success: function () {
                    	//Ontologia.load_onto_tree();
                    	$('#ontoTree').jstree(true).destroy();
                    	Ontologia.load_onto_tree();
                    	//$('#ontoTree').jstree(true).refresh();
                    }
                });
            	
            })
            //.on("activate_node.jstree", function (e, data) { console.log(data); })
            .on("select_node.jstree", function (e, data) { 
            	console.log(data);
            	
            	$.ajax({
                    url: root + "conocimiento/guardar",
                    type: "POST",
                    data: {
                    	nombre: data.node.text,
                    	URI: data.node.a_attr.unique,
                    	cargoId: $("#hdn_cargoid").val()
                    },
                    success: function () {
                    	//Ontologia.load_onto_tree();
                    	$('#ontoTree').jstree(true).destroy();
                    	Ontologia.load_onto_tree();
                    	//$('#ontoTree').jstree(true).refresh();
                    }
                });
            
            
            })
            //.on('changed.jstree', function (e, data) {
            //	alert(data.node.a_attr.es_clase);
                /*if(data.node.a_attr.data_es_modal == 1){

                    $.post(root + "indicador/pv_modal_indicador",
                    {
                        contextid: data.node.id                      
                    },
                    function(data, status){                    
                        $('#modal_indicador .modal-content').html(data);
                        $("#modal_indicador").modal({backdrop: 'static', keyboard: false});
                    });
                }*/
            //})
            .jstree({

                'core' : {
                    'check_callback' : true,
                    'themes' : {
                        'responsive': false
                    },
                    'data' : {
                        'url' : root + "getontojstree",
                        'data' : function (node) {
                            return { 
                            	'id' 	: node.id,
                            	'cargo'	: $("#hdn_cargoid").val()
                            };
                        },
                        "dataType" : "json" // needed only if you do not supply JSON headers
                    },
                    "multiple": true
                },
                "types" : {
                    'default' : {
                        'icon' : 'icofont icofont-folder'
                    },
                    'file' : {
                        'icon' : 'icofont icofont-file-alt'
                    }
                },
                "plugins" : ['types','checkbox']
            });
        }
    }

} ();


$(document).ready(function () {	
	Ontologia.load_onto_tree();
});