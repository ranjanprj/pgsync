$(document).ready(function(){
   for(var i =0; i<100; i++){
       $("body").append("<div style='float:left;padding:2px;width:80px;height:80px;' id="+i+">Calling - " + i  + "<img src='ajax-loader.gif'/></div>");
       invokeExpensiveFunc(i);
   }
});

function invokeExpensiveFunc(num){
      $.ajax({
        url : "/pgaync/PGServlet",
        method: "GET",
        async:true


    }).done(function(data){
        var sel = "#" + num;
        var html = "<pre>Num:" + data.random_num + "<BR>HPF:" + data.hpf + " <BR/>" + new Date().getSeconds() + "</pre>";
        $(sel).css({"background-color":"lightgreen"}).html(html).fadeIn();
    });
}