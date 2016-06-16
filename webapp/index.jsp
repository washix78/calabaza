<%@page import="calabaza.CalabazaSingletonManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="calabaza.CalabazaLogic"%>
<%@page import="calabaza.CalabazaPage"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
CalabazaPage userPage = (session.getAttribute("page") == null)
        ? new CalabazaPage() : ((CalabazaPage) session.getAttribute("page"));
session.setAttribute("page", userPage);
if (request.getParameter("no") != null) {
    userPage.changePageNo(Integer.parseInt(request.getParameter("no")));
}
List<String> imageList = userPage.getDisplayImageList(false);
List<String> displayTagList = userPage.getDisplayTagList();

CalabazaLogic logic = CalabazaSingletonManager.getInstance().getLogic();
List<String> tagList = logic.getTagList();
Map<String, String> encodedTagMap = logic.getEncodedTagMap();
Map<String, List<String>> imageTagListMap = logic.getImageDataMap(imageList);

StringBuffer sb = null;
int cellCount = -1;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カラバサ</title>
<link rel="stylesheet" href="./css/main.css">
<script src="./js/lib/jquery-2.2.3.min.js"></script>
<script>
var Calabaza = {};
Calabaza.tagList = <%=new JSONArray(tagList).toString()%>;
Calabaza.encMap = <%=new JSONObject(encodedTagMap).toString()%>
Calabaza.imageList = <%=new JSONArray(imageList).toString()%>;
Calabaza.imageTagListMap = <%= new JSONObject(imageTagListMap).toString()%>;
</script>
<script src="./js/main.js"></script>
</head>
<body>

<div id="main">
  <header>
    <div class="float_wrapper">
      <div class="left"><span>カラバサ</span><span>V1.0.0</span></div>
      <div class="right"><a>表示設定</a><a href="./mini/index.html" target="_new">サンプル</a></div>
    </div>
    <div><span><%= imageList.size() %> 件</span></div>
    <nav>
      <%
      for (int i = 1, pageCount = userPage.getPageCount(), currentNo = userPage.getCurrentNo(); i <= pageCount; i++) {
          if (i == currentNo) {
              out.println("<a href=\"./index.jsp?no=" + i + "\" class=\"current\">" + i + "</a>");
          } else {
              out.println("<a href=\"./index.jsp?no=" + i + "\">" + i + "</a>");
          }
      }
      %>
    </nav>
  </header>
  <ul id="image_menu">
    <%
    for (int imageI = 0, imageCount = imageList.size(); imageI != imageCount; imageI++) {
        String image = imageList.get(imageI);
        List<String> imageTagList = imageTagListMap.get(image);
        out.println("<li>");
        out.println("<div class=\"display\" data-id=\"" + imageI + "\"><img src=\"./mini/image/" + image + "\"></div>");
        out.println("<ul class=\"group tags\">");
        for (String tag : imageTagList) {
            out.println("<li>" + encodedTagMap.get(tag) + "</li>");
        }
        out.println("</ul>");
        out.println("</li>");
    }
    %>
  </ul>
</div>

<div id="overlay" class="hidden"></div>

<div id="conf_content" class="container hidden">
  <header>表示設定</header>

  <div class="table">
    <div class="row">
      <div class="cell"><span>表示件数</span></div>
      <div class="cell">
        <select>
          <%
          for (int no : new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100 }) {
              if (userPage.getDisplayCount() == no) {
                  out.println("<option value=\"" + no + "\" selected>" + no + " 件</option>");
              } else {
                  out.println("<option value=\"" + no + "\">" + no + " 件</option>");
              }
          }
          %>
        </select>
      </div>
    </div>
    <div class="row">
      <div class="cell"><span>表示する画像の種類</span></div>
      <div class="cell">
        <ul class="group">
          <%
          if (userPage.getDisplayTagList() == null) {
              out.println("<li><label><input type=\"checkbox\" checked><span>全ての画像を表示する。</span></label></li>");
              out.println("<li class=\"hidden\"><label><input type=\"checkbox\"><span>タグが無い画像を表示する。</span></label></li>");
          } else {
              out.println("<li><label><input type=\"checkbox\"><span>全ての画像を表示する。</span></label></li>");
              if (userPage.getDisplayTagList().contains(null)) {
                  out.println("<li><label><input type=\"checkbox\" checked><span>タグが無い画像を表示する。</span></label></li>");
              } else {
                  out.println("<li><label><input type=\"checkbox\"><span>タグが無い画像を表示する。</span></label></li>");
              }
          }
          %>
        </ul>

        <div class="table tags">
          <%
          sb = new StringBuffer();
          cellCount = 0;
          for (int tagI = 0, tagCount = tagList.size(); tagI != tagCount; tagI++) {
              cellCount += 1;
              if (cellCount % 2 == 1) {
                  sb.append("<div class=\"row\">");
              }

              String tag = tagList.get(tagI);
              if (displayTagList != null && displayTagList.contains(tag)) {
                  sb.append("<div class=\"cell\"><span class=\"selected\" data-id=\"")
                          .append(tagI).append("\">").append(encodedTagMap.get(tag)).append("</span></div>");
              } else {
                  sb.append("<div class=\"cell\"><span data-id=\"")
                          .append(tagI).append("\">").append(encodedTagMap.get(tag)).append("</span></div>");
              }

              if (cellCount % 2 == 0) {
                  sb.append("</div>");
              }
          }
          if (cellCount % 2 == 1) {
              sb.append("<div class=\"cell\"></div></div>");
          }
          out.println(sb.toString());
          %>
          <!--
          <div class="row">
            <div class="cell"><span data-id="0">TAG 1</span></div>
            <div class="cell"><span data-id="1">TAG 2</span></div>
          </div>
          <div class="row">
            <div class="cell"><span data-id="2">TAG 1</span></div>
            <div class="cell"><span data-id="3">TAG 2</span></div>
          </div>
          <div class="row">
            <div class="cell"><span data-id="4">TAG 1</span></div>
            <div class="cell"><span data-id="5">TAG 2</span></div>
          </div>
          <div class="row">
            <div class="cell"><span data-id="6">TAG 1</span></div>
            <div class="cell"><span data-id="7">TAG 2</span></div>
          </div>
          <div class="row">
            <div class="cell"><span data-id="8" class="selected">TAG 1</span></div>
            <div class="cell"></div>
          </div>
          -->

        </div><!-- div class="table tags" -->
      </div>
    </div>
  </div>

  <footer>
    <div class="buttons"><button>保存</button><button>キャンセル</button></div>
  </footer>
</div>

<div id="edit_content" class="container hidden">
  <header>画像タグ設定</header>

  <div class="table">
    <div class="row">
      <div class="cell">
        <div><img src=""></div>
      </div>
      <div class="cell">
        <div class="table tags">
          <!--
          <div class="row">
            <div class="cell"><input type="checkbox"><input type="text" value="TAG 1"><button>削除</button></div>
            <div class="cell"><input type="checkbox"><input type="text" value="TAG 2"><button>削除</button></div>
          </div>
          <div class="row">
            <div class="cell"><input type="checkbox"><input type="text" value="TAG 1"><button>DELE</button></div>
            <div class="cell"><input type="checkbox"><input type="text" value="TAG 2"><button>DELE</button></div>
          </div>
          <div class="row">
            <div class="cell selected" data-id="3"><input type="checkbox"checked><input type="text" value="TAG 1"><button>DELE</button></div>
            <div class="cell"></div>
          </div>
          -->
        </div>
        <ul class="group">
          <li><input type="text" placeholder="新しいタグ"><button>追加</button></li>
        </ul>
      </div>
    </div>
  </div>

  <footer>
    <div class="buttons"><button>保存</button><button>キャンセル</button></div>
  </footer>
</div>

</body>
</html>
