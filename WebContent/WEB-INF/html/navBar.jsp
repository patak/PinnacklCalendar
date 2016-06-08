<jsp:directive.page contentType="text/html; charset=ISO-8859-1" />
<nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="home">Home</a></li>
            <% if (request.getSession().getAttribute("userSession") != null){ %>
            	<li><a href="logout">Logout</a></li>
            <% } else { %>
            	<li><a href="login">Login</a></li>
            <% } %>
            <li><a href="list">List user</a></li>
            <% if (request.getSession().getAttribute("userSession") != null){ %>
            	<li><a href="change">Change Password</a></li>
            <% } else { %>
            	<li><a href="create">Create User</a></li>
            <% } %>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>