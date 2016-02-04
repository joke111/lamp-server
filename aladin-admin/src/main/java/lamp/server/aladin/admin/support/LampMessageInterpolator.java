package lamp.server.aladin.admin.support;

import lamp.server.aladin.utils.VariableReplaceUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.validation.MessageInterpolator;
import java.util.Locale;
import java.util.Objects;


public class LampMessageInterpolator implements MessageInterpolator {

	private final MessageInterpolator targetInterpolator;

	public LampMessageInterpolator(MessageInterpolator targetInterpolator) {
		Objects.requireNonNull(targetInterpolator, "Target MessageInterpolator must not be null");
		this.targetInterpolator = targetInterpolator;
	}

	@Override public String interpolate(String messageTemplate, MessageInterpolator.Context context) {
		return interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
	}

	@Override public String interpolate(String messageTemplate, MessageInterpolator.Context context, Locale locale) {
		String message = targetInterpolator.interpolate(messageTemplate, context, locale);
		return VariableReplaceUtils.replaceVariables(message, context);
	}
}
