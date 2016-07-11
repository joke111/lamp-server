package lamp.admin.web.monitoring.controller;

import lamp.monitoring.core.notification.sms.SmsNotificationAction;
import lamp.admin.web.MenuConstants;
import lamp.admin.web.support.annotation.MenuMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@MenuMapping(MenuConstants.MONITORING_ALERT_ACTIONS)
@Controller
@RequestMapping(path = "/monitoring/alert-actions", params = "type=sms")
public class SmsNotificationActionController extends GenericAlertActionController<SmsNotificationAction> {

	@Override protected String getType() {
		return "sms";
	}

}
