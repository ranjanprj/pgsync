sap.ui.controller("pgrestui5.Booking", {

/**
* Called when a controller is instantiated and its View controls (if available) are already created.
* Can be used to modify the View before it is displayed, to bind event handlers and do other one-time initialization.
* @memberOf pgrestui5.Booking
*/
	onInit: function() {
		var that = this;
		 var ws = new WebSocket("ws://localhost:8084/pgaync/chat/two");
         ws.onopen = function () {
         };
         ws.onmessage = function (message) {
        	 	var oModel = new sap.ui.model.json.JSONModel();
        	 	oModel.setData(JSON.parse(message.data));
        	 	console.log(oModel.getData());
        	 	that.getView().setModel(oModel,"msg");
//        	 	console.log(that.getView().getModel("msg"));
         };
         
//         return;
//		var that = this;
//		var oModel = new sap.ui.model.json.JSONModel();
//		oModel.loadData("http://localhost:3000/booking");
//		oModel.attachRequestCompleted(function() {
//	        console.log(oModel.getData());
//	        that.getView().setModel(oModel,"booking");
//	        console.log(that.getView().getModel("booking"));
//	    });
//		var dat = {
//			
//				status: "canceled",
//				booking_id: 1,
//				booked_by: "test@test.com",
//				service_id: 1,
//				assigned_asset: 0,
//				booked_lat: 18.4593919,
//				booked_lon: 73.8893203,
//				starting_loc: 73.8893203,
//				destination_loc: 8893203,
//				error_log: 8893203
//				};
//		
//	var booking = {			
//			
//			booked_by: "test@test.com"
//	};
//			
//		 var aData = jQuery.ajax({
//	            type : "POST",
//	            contentType : "application/json",
//	            url : "http://localhost:3000/booking",
//	            dataType : "json",
//	            data : JSON.stringify(booking),
//	            async: false, 
//	            success : function(data,textStatus, jqXHR) {
//	                
//	                alert("success to post");
//	            }
//
//	        });

	},

/**
* Similar to onAfterRendering, but this hook is invoked before the controller's View is re-rendered
* (NOT before the first rendering! onInit() is used for that one!).
* @memberOf pgrestui5.Booking
*/
	onBeforeRendering: function() {

	},

/**
* Called when the View has been rendered (so its HTML is part of the document). Post-rendering manipulations of the HTML could be done here.
* This hook is the same one that SAPUI5 controls get after being rendered.
* @memberOf pgrestui5.Booking
*/
	onAfterRendering: function() {

	},

/**
* Called when the Controller is destroyed. Use this one to free resources and finalize activities.
* @memberOf pgrestui5.Booking
*/
	onExit: function() {

	}

});