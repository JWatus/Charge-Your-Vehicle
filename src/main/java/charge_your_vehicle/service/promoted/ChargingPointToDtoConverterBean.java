package charge_your_vehicle.service.promoted;

import charge_your_vehicle.dto.ChargingPointDto;
import charge_your_vehicle.model.ChargingPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChargingPointToDtoConverterBean {

    private PromotedChargingPointsBean promotedChargingPointsBean;

    public ChargingPointToDtoConverterBean(PromotedChargingPointsBean promotedChargingPointsBean) {
        this.promotedChargingPointsBean = promotedChargingPointsBean;
    }

    public List<ChargingPointDto> convertList(List<ChargingPoint> cp) {
        return setPromotedOnList(ChargingPointDto.convertFromChargingPointList(cp));
    }

    public List<ChargingPointDto> setPromotedOnList(List<ChargingPointDto> cpd) {
        cpd.forEach(s -> s.setPromoted(promotedChargingPointsBean.isPromoted(s.getId())));
        return cpd;
    }
}
