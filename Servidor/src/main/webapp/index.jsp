<html>
<head>
    <link rel="shortcut icon" href="./img/favicon.ico">

  <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>TanSocial</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.css" rel="stylesheet">

    <!-- Add custom CSS here -->
    <link href="css/stylish-portfolio.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
    
    <link rel="stylesheet" href="style.css" type="text/css">
    <script src="js/OpenLayers.js"></script>
<script>

var size = new OpenLayers.Size(21,25);
var offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
var icon = new OpenLayers.Icon('http://www.openlayers.org/dev/img/marker.png',size,offset);
var markers = new OpenLayers.Layer.Markers( "Icono" );
var fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
var toProjection   = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
var zoom           = 17;

function gpsBrowser(){
	if (typeof navigator.geolocation == 'object'){
    	navigator.geolocation.getCurrentPosition(mostrar_ubicacion);
    }

    function mostrar_ubicacion(p)
    {
        agregarPunto(p.coords.longitude, p.coords.latitude)
    }
}

function init(lon, lat){
	
    var position = new OpenLayers.LonLat(lon, lat).transform( fromProjection, toProjection);
 
    map = new OpenLayers.Map("Map");
    var mapnik = new OpenLayers.Layer.OSM();
    map.addLayer(mapnik);
    map.setCenter(position, zoom);
    map.addLayer(markers);
    markers.addMarker(new OpenLayers.Marker(position,icon));

}

function agregarPunto(lon, lat){
    var halfIcon = icon.clone();
    markers.addMarker(new OpenLayers.Marker(new OpenLayers.LonLat(lon, lat).transform( fromProjection, toProjection),halfIcon));
}

function obtenerCamino(){
    $.ajax({
    	dataType: "json",
    	url: "http://localhost:8080/Servidor/webresources/camino/consultarCamino",
    	//Leer asyn 
        beforeSend: function () {
                //$("#Menu").html("Procesando, espere por favor...");
        },
        success:  function (response) {
        		//$("#Menu").html("Log actualizado");
        		//$("#log").html(response.items);
	    		var stringParts = response.items[0].split("#");
    			agregarPunto(stringParts[0].split("[")[1],stringParts[1].split("]")[0]);

        		
        }
});
}

//setInterval('obtenerCamino()', 2000);

</script>

</head>

<style>
</style>
<body onload="init(-59.1202,-37.3232); obtenerCamino();">
<!-- Side Menu -->
    <a id="menu-toggle" href="#" class="btn btn-primary btn-lg toggle"><i class="fa fa-reorder"></i></a>
    <div id="sidebar-wrapper">
      <ul class="sidebar-nav">
        <a id="menu-close" href="#" class="btn btn-default btn-lg pull-right toggle"><i class="fa fa-times"></i></a>
        <li class="sidebar-brand"><a href="http://startbootstrap.com">TanSocial</a></li>
        <li><a href="#top">Inicio</a></li>
        <li><a href="#about">Acerca de...</a></li>
        <li><a href="#services">Funcionalidades</a></li>
        <li><a href="#portfolio">Utilidad</a></li>
        <li><a href="#contact">Caminos monitoreados</a></li>
      </ul>
    </div>
    <!-- /Side Menu -->
  
    <!-- Full Page Image Header Area -->
    <div id="top" class="header">
      <div class="vert-text">
        <h1>TanSocial</h1>
        <h3><em>Tesis para encuestas tipo Origen Destino</h3>
        <a href="#about" class="btn btn-default btn-lg">Leer mas</a>
      </div>
    </div>
    <!-- /Full Page Image Header Area -->
  
    <!-- Intro -->
    <div id="about" class="intro">
      <div class="container">
        <div class="row">
          <div class="col-md-6 col-md-offset-3 text-center">
            <h2>Facultad de Ciencias Exactas Universidad Nacional del Centro</h2>
            <p class="lead">Trabajo final de Ingenieria en Sistema <br> Manzanel, Juan Manuel - Mendiola, Jorge Carlos</p>
          </div>
        </div>
      </div>
    </div>
    <!-- /Intro -->
  
    <!-- Services -->
    <div id="services" class="services">
      <div class="container">
        <div class="row">
          <div class="col-md-4 col-md-offset-4 text-center">
            <h2>Funcionalidades</h2>
            <hr>
          </div>
        </div>
        <div class="row">
          <div class="col-md-2 col-md-offset-2 text-center">
            <div class="service-item">
              <i class="service-icon fa fa-rocket"></i>
              <h4>Monitoreo Offline</h4>
              <p>La aplicacion permite realizar el monitoreo de un camino offline el cual luego podra ser almancenado en un servidor.</p>
            </div>
          </div>
          <div class="col-md-2 text-center">
            <div class="service-item">
              <i class="service-icon fa fa-magnet"></i>
              <h4>Generacion de Atributos</h4>
              <p>A un punto determinado se puede indicar un atributo tal como un lugar, algun eventos entre otras.</p>
            </div>
          </div>
          <div class="col-md-2 text-center">
            <div class="service-item">
              <i class="service-icon fa fa-shield"></i>
              <h4>Aplicacion Movil</h4>
              <p>Esta aplicacion La misma va a permite la portabilidad necesaria para llevar una encuesta origen destino en nuestra palma de la mano.</p>
            </div>
          </div>
          <div class="col-md-2 text-center">
            <div class="service-item">
              <i class="service-icon fa fa-pencil"></i>
              <h4>Mineria de Datos</h4>
              <p>A partir de la generacion de datos realizada por la aplicacion se podra hacer mineria de datos para diversos propositos.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- /Services -->

    <!-- Callout -->
    <div class="callout">
      <div class="vert-text">
        <h1>Mejorando la calidad de los Usuarios</h1>
      </div>
    </div>
    <!-- /Callout -->

    <!-- Portfolio -->
    <div id="portfolio" class="portfolio">
      <div class="container">
        <div class="row">
          <div class="col-md-4 col-md-offset-4 text-center">
            <h2>En cualquier lugar del mundo</h2>
            <hr>
          </div>
        </div>
        <div class="row">
          <div class="col-md-4 col-md-offset-2 text-center">
            <div class="portfolio-item">
              <a href="#"><img class="img-portfolio img-responsive" src="img/portfolio-1.jpg"></a>
              <h4>Ciudades</h4>
            </div>
          </div>
          <div class="col-md-4 text-center">
            <div class="portfolio-item">
              <a href="#"><img class="img-portfolio img-responsive" src="img/portfolio-2.jpg"></a>
              <h4>Zonas Rurales</h4>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-md-4 col-md-offset-2 text-center">
            <div class="portfolio-item">
              <a href="#"><img class="img-portfolio img-responsive" src="img/portfolio-3.jpg"></a>
              <h4>Rutas</h4>
            </div>
          </div>
          <div class="col-md-4 text-center">
            <div class="portfolio-item">
              <a href="#"><img class="img-portfolio img-responsive" src="img/portfolio-4.jpg"></a>
              <h4>Turismo</h4>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- /Portfolio -->

    <!-- Call to Action -->
    <div class="call-to-action">
      <div class="container">
        <div class="row">
          <div class="col-md-6 col-md-offset-3 text-center">
            <h3>La documentacion de la arquitectura y el codigo del proyecto se encuentran en </h3>
            <a href="https://github.com/wrtfix/Tansocial" class="btn btn-lg btn-primary">GitHub!</a>
          </div>
        </div>
      </div>
    </div>
    <!-- /Call to Action -->

    <!-- Map -->
    <div id="contact" class="map">
        <div id="Map" style="height:550px; width:100%"></div>
    </div>
    <!-- /Map -->
    
    <!-- Footer -->
    <footer>
      <div class="container">
        <div class="row">
          <div class="col-md-6 col-md-offset-3 text-center">
            <ul class="list-inline">
              <li><i class="fa fa-facebook fa-3x"></i></li>
              <li><i class="fa fa-twitter fa-3x"></i></li>
              <li><i class="fa fa-dribbble fa-3x"></i></li>
            </ul>
            <div class="top-scroll">
              <a href="#top"><i class="fa fa-circle-arrow-up scroll fa-4x"></i></a>
            </div>
            <hr>
            <p>Copyright &copy; TanSocial 2013</p>
          </div>
        </div>
      </div>
    </footer>
    <!-- /Footer -->

    <!-- Bootstrap core JavaScript -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>

    <!-- Custom JavaScript for the Side Menu and Smooth Scrolling - Put in a custom JavaScript file to clean this up -->
    <script>
        $("#menu-close").click(function(e) {
            e.preventDefault();
            $("#sidebar-wrapper").toggleClass("active");
        });
    </script>
    <script>
        $("#menu-toggle").click(function(e) {
            e.preventDefault();
            $("#sidebar-wrapper").toggleClass("active");
        });
    </script>
    <script>
      $(function() {
        $('a[href*=#]:not([href=#])').click(function() {
          if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') 
            || location.hostname == this.hostname) {

            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
            if (target.length) {
              $('html,body').animate({
                scrollTop: target.offset().top
              }, 1000);
              return false;
            }
          }
        });
      });
    </script>

</body>

</html>