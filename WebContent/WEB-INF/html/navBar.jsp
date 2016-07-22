<jsp:directive.page contentType="text/html; charset=ISO-8859-1" />

<nav class="navbar navbar-inverse navbar-fixed-top">
   <div class="container">
     <div class="navbar-header">
       <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </button>
       <a class="navbar-brand" href="#">Pinnackl Calendar</a>
     </div>
     <div id="navbar" class="collapse navbar-collapse">
       <ul class="nav navbar-nav">
         <% if (request.getSession().getAttribute("userSession") != null) { %>
         	<li class="${homeTab}"><a href="home">Home</a></li>
         	<li class="${eventsTab}"><a href="events">Events</a></li>
         <% } %>
        <% if (request.getSession().getAttribute("userSession") != null){ %>
        	<li class="${changeTab}"><a href="change">Change Password</a></li>
        	<li><a href="logout">Logout</a></li>
        <% } else { %>
        	<li class="${loginTab}"><a href="login">Login</a></li>
        	<li class="${createTab}"><a href="create">Sign In</a></li>
        <% } %>
       </ul>
     </div><!--/.nav-collapse -->
   </div>
 </nav>