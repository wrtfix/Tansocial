<html>
<head>
    <title>TanSocial Server</title>
</head>
<link rel="shortcut icon" href="./image/favicon.ico">
<script src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
<script>
function obtenerCamino(){
    $.ajax({
  		url: "http://localhost:8080/Servidor/webresources/camino/consultarCamino",
  	    type:  'get',
        beforeSend: function () {
                $("#Menu").html("Procesando, espere por favor...");
        },
        success:  function (response) {
                $("#Menu").html(response);
        }
});
}
</script>

<style>
</style>
<body onLoad="obtenerCamino()">
<p><a href="http://localhost:8080/Servidor/webresources/camino/consultarCamino">Los caminos loco</a></p>

<H1>
	<img alt="Inicio TanSocial" src="./image/Logo.png" width="40" height="35"/> TanSocial
</H1>
<div id="Menu">
</div>
	<h3>Log de Servidor:</h3>
<div>
<textarea id="log" rows="20" cols="150">
</textarea>
</div>
</body>
</html>