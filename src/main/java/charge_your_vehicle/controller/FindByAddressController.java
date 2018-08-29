package charge_your_vehicle.controller;

import charge_your_vehicle.model.dto.AddressDto;
import charge_your_vehicle.service.providers.FindByAddressModelsProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindByAddressController {

    private final FindByAddressModelsProvider findByAddressModelsProvider;

    public FindByAddressController(FindByAddressModelsProvider findByAddressModelsProvider) {
        this.findByAddressModelsProvider = findByAddressModelsProvider;
    }

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestByAddressFormPage() {
        return findByAddressModelsProvider.getModelAndViewFindTheClosestByAddressFormPage();
    }

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        return findByAddressModelsProvider.getModelAndViewFindTheClosestByAddressResultPage(addressDto);
    }

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestInRadiusByAddressFormPage() {
        return findByAddressModelsProvider.getModelAndViewFindTheClosestInRadiusByAddressFormPage();
    }

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestInRadiusByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        return findByAddressModelsProvider.getModelAndViewFindTheClosestInRadiusByAddressResultPage(addressDto);
    }
}
