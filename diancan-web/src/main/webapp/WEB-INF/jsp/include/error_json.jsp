<%@page session="false"%>
<%@ page language="java" contentType="application/json; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	response.setStatus(200);
    String outStr = (String) request.getAttribute("respJson");
	out.print(outStr);
%>
