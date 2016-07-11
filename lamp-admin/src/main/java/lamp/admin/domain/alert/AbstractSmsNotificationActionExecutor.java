package lamp.admin.domain.alert;

import lamp.admin.domain.alert.model.SmsNotificationAction;
import lamp.common.monitoring.model.Tenant;
import lamp.common.monitoring.model.TenantUser;
import lamp.common.utils.StringUtils;
import lamp.monitoring.core.alert.model.AlertActionContext;
import lamp.monitoring.core.alert.model.AlertEvent;
import lamp.monitoring.core.notification.NotificationMessage;
import lamp.monitoring.core.notification.NotificationSender;
import lamp.monitoring.core.notification.mms.SmsHttpSender;
import lamp.monitoring.core.notification.mms.SmsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractSmsNotificationActionExecutor<M extends NotificationMessage, A extends SmsNotificationAction>
	extends NotificationActionExecutor<A> {

	private NotificationSender<M> notificationSender;

	public AbstractSmsNotificationActionExecutor(NotificationSender<M> notificationSender) {
		this.notificationSender = notificationSender;
	}

	@Override
	protected void doExecute(A action, AlertActionContext context) {
		AlertEvent alertEvent = context.getAlertEvent();

		M message = newMessage(action, context);

		List<String> mobilePhoneNumbers = getMobilePhoneNumbers(action, alertEvent);
		for (String mobilePhoneNumber : mobilePhoneNumbers) {
			try {
				M sendMessage = newMessageWithPhoneNumber(message, mobilePhoneNumber);

				notificationSender.send(sendMessage);
			} catch (Exception e) {
				log.warn("Notification send failed", e);
			}
		}
	}

	protected abstract M newMessageWithPhoneNumber(M message, String mobilePhoneNumber);

	protected abstract M newMessage(A action, AlertActionContext context);

	protected List<String> getMobilePhoneNumbers(A action, AlertEvent alertEvent) {
		List<String> mobilePhoneNumbers = new ArrayList<>();
		Tenant tenant = alertEvent.getTenant();
		if (tenant != null) {
			Optional<List<TenantUser>> usersOptional = Optional.ofNullable(tenant.getUsers());
			mobilePhoneNumbers.addAll(
				usersOptional.orElse(Collections.emptyList()).stream()
					.map(TenantUser::getMobilePhoneNumber)
					.filter(StringUtils::isNotBlank)
					.collect(Collectors.toList())
			);
		}

		if (StringUtils.isNotBlank(action.getPhoneNumbers())) {
			mobilePhoneNumbers.addAll(
				Arrays.stream(StringUtils.split(action.getPhoneNumbers(), ","))
					.map(StringUtils::trim)
					.filter(StringUtils::isNotBlank)
					.collect(Collectors.toList())
			);
		}
		return mobilePhoneNumbers;
	}

}
