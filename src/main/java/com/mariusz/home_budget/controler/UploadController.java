package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.CsvParser;
import com.mariusz.home_budget.service.FinancialService;
import com.mariusz.home_budget.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class UploadController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    StorageService storageService;

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
@Autowired
    public UploadController(AuthenticationFacade authenticationFacade, FinancialService financialService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }


    // CHANGE IT ACCORDING TO YOUR LOCATION
  //  private final String UPLOAD_FILE_LOCATION="/resources";

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "File can't be empty.\n Please select a file to upload");
            return "redirect:uploadStatus";
        }

        AppUser user = authenticationFacade.getApplicationUser();
        MoneyHolder holder = financialService.getMoneyHolders(user).get(0);
        CsvParser parser = new CsvParser(file, user, holder.getId()+"");
        List<MoneyFlowForm> moneyFlows = parser.printCsv();
        if (!moneyFlows.isEmpty()){
            logger.info("Number of operations: "+moneyFlows.size());
            for (MoneyFlowForm form : moneyFlows
                 ) {
                logger.info("Loging "+form.toString());
               Optional<String> error= financialService.addOperation(form);
                if (error.isPresent()){
                    logger.info("Error with parsed data: "+error.get());
                }
            }
        }else {
            redirectAttributes.addFlashAttribute("message", "Incorrect date in file");
            return "redirect:uploadStatus";

        }
//
//
//
//        String UploadedFolderLocation = UPLOAD_FILE_LOCATION+"/";
//        String fileName = null;
////   this is done to work on IE as well
//        String pattern = Pattern.quote(System.getProperty("file.separator"));
//        String[] str = file.getOriginalFilename().split(pattern);
//        if (str.length > 0)
//            fileName = str[str.length - 1];
//        else
//            fileName = str[0];
//        if(!storageService.store(file, fileName,UploadedFolderLocation))
//        {
//            redirectAttributes.addFlashAttribute("message", "Error occurred while uploading the file");
//            redirectAttributes.addFlashAttribute("status", "false");
//            return "redirect:/uploadStatus";
//        }
//        redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + fileName + "'");
//        redirectAttributes.addFlashAttribute("status", "true");

        redirectAttributes.addFlashAttribute("message", "File loaded successfully ");
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus(ModelMap m) {
        return "Homepage";
    }

    @GetMapping("/upload")
    public String displayHomePageForAlarm() {
        return "Homepage";
    }


}
