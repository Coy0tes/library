<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>统计管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			showBar();
		});
		
		var myChart;
		var myChart2;
		var myChart3;
		window.onresize = function(){
		    myChart.resize(); 
		    myChart2.resize(); 
		    myChart3.resize(); 
		};
		//显示柱状图
		function showBar() {
		
			var booksAll = 0;
			var booksbooksByIn = 0;
			var booksbooksByOut = 0;
			var booksbooksByBreak = 0;
			var booksbooksByLoss = 0;
			
			$.ajax({
				url:"${ctx}/statistic/statistic/getBooks",
				type:"post",
				dataType:"json",
				async:false,
				success:function(data){
					booksAll = data.booksAll;
					booksByIn = data.booksByIn;
					booksByOut = data.booksByOut;
					booksByBreak = data.booksByBreak;
					booksByLoss = data.booksByLoss;
					
					$("#booksAll").html('<font style="font-size:16px;">'+booksAll+'</font>');
					$("#booksByIn").html('<font style="font-size:16px;">'+booksByIn+'</font>');
					$("#booksByOut").html('<font style="font-size:16px;">'+booksByOut+'</font>');
					$("#booksByBreak").html('<font style="font-size:16px;">'+booksByBreak+'</font>');
					$("#booksByLoss").html('<font style="font-size:16px;">'+booksByLoss+'</font>');
				}
			});
			
			
			var ecdiv = document.getElementById('pieChart');
			myChart3 = echarts.init(ecdiv);
			myChart3.setOption({
				
			    /* title : {
			        text: '图书馆图书状态统计',
			        /* subtext: '纯属虚构', */
			        /*x:'center'
			    }, */
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} 本(占{d}%)"
			    },
			    legend: {
			        orient: 'vertical',
			        left: 'left',
			        data: ['所有图书','在库图书','外借图书','损坏图书','遗失图书']
			    },
			    series : [
			        {
			            name: '图书统计',
			            type: 'pie',
			            radius : '85%',
			            center: ['50%', '50%'],		// 调整图形位置
			            data:[
			                {value:booksByIn, name:'在库图书'},
			                {value:booksByOut, name:'外借图书'},
			                {value:booksByBreak, name:'损坏图书'},
			                {value:booksByLoss, name:'遗失图书'}
			            ],
			            itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            }
			        }
			    ]
			});
		}

	</script>
	<script src="${ctxStatic}/echarts/echarts.min.js" charset="utf-8" type="text/javascript"></script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>统计列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <form:form modelAttribute="agencyStatistic">
    <!-- 图书状态统计 -->
	<div class="ibox-content" style="padding: 15px 20px 10px 20px;">
		<div class="row">
			<div class="col-sm-12">
	    	<div class="ibox float-e-margins">
	            <h4>图书状态统计</h4>
	            <div class="ibox-content">
	            
	            	<!-- 饼形图加载 -->
	            	<div id="pieChart" style="width: 100%;height:400px;margin-top:20px;">
					
					</div>
				
				
				<table id="contentTable" class="table table-bordered table-hover table-condensed dataTables-example dataTable">
							<tr>
								<th>图书状态</th>
								<th>图书数量</th>
								<th>明细</th>
							</tr>
						<tbody>
							<tr>
								<td>全部图书</td>
								<td><span id="booksAll"></span></td>
								<th><a href="#" onclick="top.openTab('${ctx}/books/books','全部图书',true)">全部图书明细</a></th>
							</tr>
							<tr>
								<td>在库图书</td>
								<td><span id="booksByIn"></span></td>
								<th><a href="#" onclick="top.openTab('${ctx}/books/books?status=0','全部图书',true)">在库图书明细</a></th>
							</tr>
							<tr>
								<td>借出图书</td>
								<td><span id="booksByOut"></span></td>
								<th><a href="#" onclick="top.openTab('${ctx}/books/books?status=1','全部图书',true)">借出图书明细</a></th>
							</tr>
							<tr>
								<td>损坏图书</td>
								<td><span id="booksByBreak"></span></td>
								<th><a href="#" onclick="top.openTab('${ctx}/books/books?status=2','全部图书',true)">损坏图书明细</a></th>
							</tr>
							<tr>
								<td>遗失图书</td>
								<td><span id="booksByLoss"></span></td>
								<th><a href="#" onclick="top.openTab('${ctx}/books/books?status=3','全部图书',true)">遗失图书明细</a></th>
							</tr>
						</tbody>
					</table>
	            </div>
	         </div>
	         </div>
	     </div>
	    </div>
	   </form:form>
	</div>
</div>
</body>
</html>