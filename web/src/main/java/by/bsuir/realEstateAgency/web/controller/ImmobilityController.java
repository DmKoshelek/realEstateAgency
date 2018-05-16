package by.bsuir.realEstateAgency.web.controller;

import by.bsuir.realEstateAgency.core.service.ImmobilityService;
import by.bsuir.realEstateAgency.web.bean.immobility.ImmobilityDto;
import by.bsuir.realEstateAgency.web.bean.pagedList.CheckedItem;
import by.bsuir.realEstateAgency.web.bean.pagedList.CheckedList;
import by.bsuir.realEstateAgency.web.exceptions.BadRequestException;
import by.bsuir.realEstateAgency.web.exceptions.NotFoundException;
import by.bsuir.realEstateAgency.web.facade.ImmobilityFacade;
import by.bsuir.realEstateAgency.web.security.AuthUserDetails;
import by.bsuir.realEstateAgency.web.service.page.PageService;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/immobilities")
public class ImmobilityController {

    static Logger log = Logger.getLogger(ImmobilityController.class.getName());

    private static final String PAGE_NUMBER_REQUEST_PARAM = "page";

    private static final String PAGED_LIST_ATTRIBUTE = "pagedList";

    private static final String CREATE_USER_ATTRIBUTE = "create";

    private static final String IMMOBILITY_DTO_ATTRIBUTE = "immobilityDto";

    @Resource
    private ImmobilityFacade immobilityFacade;

    @Resource
    private ImmobilityService immobilityService;

    @Resource
    private PageService pageService;

    @GetMapping
    private String getImmobilities(@RequestParam(name = PAGE_NUMBER_REQUEST_PARAM, defaultValue = "1") int pageNumber,
                                   Model model) {
        model.addAttribute(PAGED_LIST_ATTRIBUTE, pageService.getPagedList(pageNumber, immobilityService));
        return "immobilitiesList";
    }

    @PostMapping(params = "remove")
    private String removeImmobilities(@RequestParam(name = PAGE_NUMBER_REQUEST_PARAM, defaultValue = "1") int pageNumber,
                                      CheckedList checkedList, BindingResult bindingResult,
                                      Authentication authentication, Model model) throws Exception {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            log.error("Bad request params for deleting Users. HTTP400");
            throw new BadRequestException();
        }
        if (checkedList.getCheckedList() != null) {
            immobilityService.removeList(checkedList.getCheckedList()
                    .stream().filter(CheckedItem::isChecked)
                    .map(CheckedItem::getId).collect(Collectors.toList()), userDetails.getUser());
        }
        return "redirect:/immobilities?page=" + pageNumber;
    }

    @GetMapping("/new")
    public String getNewForm(Model model) {
        model.addAttribute(CREATE_USER_ATTRIBUTE, true);
        model.addAttribute(IMMOBILITY_DTO_ATTRIBUTE, new ImmobilityDto());
        return "immobilityDetails";
    }

    @PostMapping("/new")
    public String createImmobility(@Valid ImmobilityDto immobilityDto, BindingResult bindingResult,
                                   Authentication authentication, Model model) {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            model.addAttribute(CREATE_USER_ATTRIBUTE, true);
            return "immobilityDetails";
        }
        if (immobilityDto.getUploadedFiles() != null && immobilityDto.getUploadedFiles().size() > 0) {
            immobilityDto.getUploadedFiles().remove(0);
        }
        immobilityFacade.saveOrUpdate(immobilityDto, userDetails.getUser());
        return "redirect:/immobilities/" + immobilityDto.getId();
    }

    @GetMapping("/{id}")
    public String getImmobility(@PathVariable("id") long id, Model model) {
        ImmobilityDto immobilityDto = immobilityFacade.getImmobility(id);
        if (immobilityDto == null) {
            log.error("User not found. HTTP404");
            throw new NotFoundException();
        }
        model.addAttribute(IMMOBILITY_DTO_ATTRIBUTE, immobilityDto);
        return "immobilityDetails";
    }

    @PostMapping(value = "/{id}", params = "save")
    public String updateImmobility(@PathVariable("id") long id,
                                   @Valid ImmobilityDto immobilityDto, BindingResult bindingResult,
                                   Authentication authentication, Model model) {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        if (bindingResult.hasErrors()) {
            return "immobilityDetails";
        }
        if (immobilityDto.getUploadedFiles() != null && immobilityDto.getUploadedFiles().size() > 0) {
            immobilityDto.getUploadedFiles().remove(0);
        }
        immobilityFacade.saveOrUpdate(immobilityDto, userDetails.getUser());
        return "redirect:/immobilities/" + id;
    }

    @PostMapping(value = "/{id}", params = "remove")
    public String removeImmobility(@PathVariable("id") long id, Authentication authentication, Model model) {
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        immobilityService.remove(id, userDetails.getUser());
        return "redirect:/immobilities";
    }
}