/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tree.web;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.tree.entity.Testtree;
import com.jeeplus.modules.tree.service.TesttreeService;

/**
 * treeController
 * @author tree
 * @version 2017-11-01
 */
@Controller
@RequestMapping(value = "${adminPath}/tree/testtree")
public class TesttreeController extends BaseController {

	@Autowired
	private TesttreeService testtreeService;
	
	@ModelAttribute
	public Testtree get(@RequestParam(required=false) String id) {
		Testtree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testtreeService.get(id);
		}
		if (entity == null){
			entity = new Testtree();
		}
		return entity;
	}
	
	/**
	 * tree列表页面
	 */
	@RequiresPermissions("tree:testtree:list")
	@RequestMapping(value = {"list", ""})
	public String list(Testtree testtree, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Testtree> page = testtreeService.findPage(new Page<Testtree>(request, response), testtree); 
		model.addAttribute("page", page);
		return "modules/tree/testtreeList";
	}

	/**
	 * 查看，增加，编辑tree表单页面
	 */
	@RequiresPermissions(value={"tree:testtree:view","tree:testtree:add","tree:testtree:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Testtree testtree, Model model) {
		model.addAttribute("testtree", testtree);
		return "modules/tree/testtreeForm";
	}

	/**
	 * 保存tree
	 */
	@RequiresPermissions(value={"tree:testtree:add","tree:testtree:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Testtree testtree, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, testtree)){
			return form(testtree, model);
		}
		if(!testtree.getIsNewRecord()){//编辑表单保存
			Testtree t = testtreeService.get(testtree.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(testtree, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			testtreeService.save(t);//保存
		}else{//新增表单保存
			testtreeService.save(testtree);//保存
		}
		addMessage(redirectAttributes, "保存tree成功");
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
	}
	
	/**
	 * 删除tree
	 */
	@RequiresPermissions("tree:testtree:del")
	@RequestMapping(value = "delete")
	public String delete(Testtree testtree, RedirectAttributes redirectAttributes) {
		testtreeService.delete(testtree);
		addMessage(redirectAttributes, "删除tree成功");
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
	}
	
	/**
	 * 批量删除tree
	 */
	@RequiresPermissions("tree:testtree:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testtreeService.delete(testtreeService.get(id));
		}
		addMessage(redirectAttributes, "删除tree成功");
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("tree:testtree:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Testtree testtree, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "tree"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Testtree> page = testtreeService.findPage(new Page<Testtree>(request, response, -1), testtree);
            
            int lengthList = page.getList().size();
//            System.out.println("lengthList:"+lengthList);
            
            String pid = "0"; // 最顶层父id
            
            // 循环数据库数据
//            for(int i = 0; i<lengthList; i++){
        	for(int i = 0; i<lengthList; i++){
            	//获得编号跟编号长度
            	String name = page.getList().get(i).getName();
//            	int nameLength = name.replace(".", "").toString().length();	// 过滤点的长度，用于if判断
            	int maxlength = name.length();		// 未过滤点，用于截取字符寻找上级
//            	System.out.println("name:"+name);
//            	System.out.println("maxlength:"+maxlength);
            	
            	// 通过判断编号长度确定第几级
            	if(maxlength == 1){
            		// 如果长度为1，父级（parent）赋值“0”
            		page.getList().get(i).setParent(pid);
            		page.getList().get(i).setSort(0);
            	}else if (maxlength>=2){									// 如果长度为3或 n 以上，循环n-1次
            		boolean a = false;
            		String upid = "";	// 存放上级id
            			
	            			Testtree t = new Testtree();
	            			String names = page.getList().get(i).getName();
	            			
	            			
	            			// 开始查找上级id，如果有特殊符号点，则过滤
	            			int j = 0;
	            			j=name.length();
	            			
	            			do{
	            				
		            			if(names.substring(j-2,j-1).equals(".")){
		            				t.setName(names.substring(0,name.length()-2));
		            			}else if(names.substring(j-1).equals("-")){
		            				t.setName(names.substring(0,name.length()-2));
		            			}else if(names.substring(j-1).equals("+")){
		            				t.setName(names.substring(0,name.length()-2));
		            			}else{
		            				t.setName(names.substring(0,j-1));
		            				if(names.substring(j-2,j-1).equals("-")){
			            				t.setName(names.substring(0,name.length()-2));
			            			}
		            			}
		            			System.out.println("names:"+names);
		            			
		                		List<Testtree> li = testtreeService.findList(t); 	// 去重复，取第一个
		                		
		                		if(li.size()!=0){
			                		if(li.get(0) != null){
			                			a=false;
			                			
			                			// 去重复，如果数据库name字段重复，则不添加
			                			Testtree rept = new Testtree();
			                			rept.setName(li.get(0).getName());
			                			List<Testtree> reptl = testtreeService.getName(rept);
			                			
			                			if(!reptl.get(0).getName().equals(page.getList().get(i).getName())){
			                				upid = li.get(0).getId();
			                				page.getList().get(i).setParent(upid);
			                				
			                				page.getList().get(i).setSort(0);
			                				
			                				System.out.println(reptl.get(0).getName());
			                				System.out.println(page.getList().get(i).getName());
			                			}
			                		}
			                		
		                		}else{
		                			a=true;
		                			j--;
		                		}

		                		System.out.println(a);
	            			}while(a);
            	}
            	System.out.println(i);
            }
            
            
            
    		new ExportExcel("tree", Testtree.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出tree记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
    }

	
	
	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("tree:testtree:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Testtree> list = ei.getDataList(Testtree.class);
			for (Testtree testtree : list){
				try{
					testtreeService.save(testtree);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条tree记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条tree记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入tree失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
    }
	
	/**
	 * 下载导入tree数据模板
	 */
	@RequiresPermissions("tree:testtree:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "tree数据导入模板.xlsx";
    		List<Testtree> list = Lists.newArrayList(); 
    		new ExportExcel("tree数据", Testtree.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/tree/testtree/?repage";
    }
	
	public static void main(String[] args) {
		
		for(int j = 0; j<2171; j++){
			System.out.println(UUID.randomUUID().toString().replace("-", ""));
		}
		
//		//          数据数量
//		int j = 29220 - 2;		
//		//          数据库总数量
//		int n = 4021;
//		for(int i = n+1; i>=0; i--){
//			j++;
//			System.out.println(j);
//		}
		
		for(int i = 0;i<55366; i++){
			System.out.println(i);
		}
		
		
	}
	

}