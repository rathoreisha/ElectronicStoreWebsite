package com.shruteekatech.ecommerce.service.impl;

import com.shruteekatech.ecommerce.constant.AppConstant;
import com.shruteekatech.ecommerce.dtos.CategoryDto;
import com.shruteekatech.ecommerce.dtos.PagableResponse;
import com.shruteekatech.ecommerce.exception.ResourcenotFoundException;
import com.shruteekatech.ecommerce.helper.Pageablemethod;
import com.shruteekatech.ecommerce.model.Category;
import com.shruteekatech.ecommerce.repository.CategoryRepo;
import com.shruteekatech.ecommerce.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
//import net.sf.jasperreports.engine.*;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    /**
     * @param categoryDto
     * @return
     */
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        log.info("Initiated Dao Call to save the Category");
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category save = this.categoryRepo.save(category);
        CategoryDto categoryDto1 = this.modelMapper.map(save, CategoryDto.class);
        log.info("Completed Dao call to save the Category");
        return categoryDto1;
    }

    /**
     * @param categorydto
     * @param catId
     * @return
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categorydto, Long catId) {
        log.info("Initiated Dao Call to Update the Category with :{}",catId);
        Category category = this.categoryRepo.findById(catId).orElseThrow(() -> new ResourcenotFoundException(AppConstant.CATEGORY, AppConstant.ID, catId));

        category.setTitle(categorydto.getTitle());
        category.setDescription(categorydto.getDescription());
        category.setCoverImage(categorydto.getCoverImage());

        Category save = this.categoryRepo.save(category);

        log.info("Completed Dao Call to Update the Category with :{}",catId);
        return this.modelMapper.map(save,CategoryDto.class);
    }

    /**
     * @param catID
     * @return
     */
    @Override
    public CategoryDto getSingleCategory(Long catID) {
        log.info("Initiated Dao Call to Get the Category with :{}",catID);
        Category category = this.categoryRepo.findById(catID).orElseThrow(() -> new ResourcenotFoundException(AppConstant.CATEGORY, AppConstant.ID, catID));
        log.info("Completed Dao Call to Get the Category with :{}",catID);
        return this.modelMapper.map(category,CategoryDto.class);
    }

    /**
     * @return
     */
    @Override
    public PagableResponse<CategoryDto> getAllcategories(Integer pagenumber, Integer pagesize, String sortBy, String sortDir) {

        log.info("Initiating dao call to get the All Category data ");

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : (Sort.by(sortBy).descending());
        Pageable page = PageRequest.of(pagenumber, pagesize, sort);
        Page<Category> categories = this.categoryRepo.findAll(page);

        PagableResponse<CategoryDto> pageableresponse = Pageablemethod.getPageableresponse(categories, CategoryDto.class);

        log.info("Completed dao call to get the All Categories data ");
        return pageableresponse;

    }

    /**
     * @param keyword
     * @return
     */
    @Override
    public List<CategoryDto> searchCategory(String keyword) {
        log.info("Initiated Dao Call to search the Category with :{}",keyword);
        List<Category> categoryList = this.categoryRepo.findAByTitleContaining(keyword);
        log.info("Completed Dao Call to Search the Category with :{}",keyword);
        return categoryList.stream().map((category) -> this.modelMapper.map(category, CategoryDto.class)).collect(Collectors.toList());
    }

    /**
     * @param catid
     */
    @Override
    public void deleteCategory(Long catid) {
        log.info("Initiated Dao Call to Delete the Category with :{}",catid);

        Category category = this.categoryRepo.findById(catid).orElseThrow(() -> new ResourcenotFoundException(AppConstant.CATEGORY, AppConstant.ID, catid));
        log.info("Completed Dao Call to Delete the Category with :{}",catid);
        this.categoryRepo.delete(category);

    }

  /*  @Override
    public String exportrept(String reportFormat) throws FileNotFoundException, JRException {
        log.info("Initiating dao call to Generate the Category report with report format:{}", reportFormat);

        String path = "F:\\Bikkadit\\ElectronicStore_Project";
        List<Category> categoryRepoAll = this.categoryRepo.findAll();
        //load file and compile it
        File file = ResourceUtils.getFile("classpath:Category.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(categoryRepoAll);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Isha Rathore");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\Category.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\Category.pdf");
        }
        if (reportFormat.equalsIgnoreCase("xml")) {
            JasperExportManager.exportReportToXmlFile(jasperPrint,path + "\\Category.xls",true);
        }
        log.info("Completed dao call to Generate the users report with report format:{}", reportFormat);

        return "report generated in path : " + path;

    }*/
}
