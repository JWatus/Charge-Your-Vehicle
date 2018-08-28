package charge_your_vehicle.controller.search;

import charge_your_vehicle.model.dto.AddressDto;
import charge_your_vehicle.service.providers.FindByAddressModelProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindByAddressController {

    private final FindByAddressModelProvider findByAddressModelProvider;

    public FindByAddressController(FindByAddressModelProvider findByAddressModelProvider) {
        this.findByAddressModelProvider = findByAddressModelProvider;
    }

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestByAddressFormPage() {
        return findByAddressModelProvider.getModelAndViewFindTheClosestByAddressFormPage();
    }

    @RequestMapping(value = "/find-the-closest-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        return findByAddressModelProvider.getModelAndViewFindTheClosestByAddressResultPage(addressDto);
    }

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.GET)
    public ModelAndView getFindTheClosestInRadiusByAddressFormPage() {
        return findByAddressModelProvider.getModelAndViewFindTheClosestInRadiusByAddressFormPage();
    }

    @RequestMapping(value = "/find-the-closest-in-radius-by-address", method = RequestMethod.POST)
    public ModelAndView getFindTheClosestInRadiusByAddressResultPage(@ModelAttribute AddressDto addressDto) {
        return findByAddressModelProvider.getModelAndViewFindTheClosestInRadiusByAddressResultPage(addressDto);
    }
}
