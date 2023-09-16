package com.lime.it.lime.it.broker.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServiceProviderImpl<T> implements ServiceProvider<T> {

    private final BeanFactory beanFactory;

    @Override
    public T provide(Class<T> tClass, String qualifier) {
        try {
            return BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, tClass, qualifier);
        } catch (Exception exception) {
            throw new ServiceProviderException(
                    String.format("Unable to proved service: %s with qualifier: %s", tClass.getName(), qualifier)
            );
        }
    }
}
