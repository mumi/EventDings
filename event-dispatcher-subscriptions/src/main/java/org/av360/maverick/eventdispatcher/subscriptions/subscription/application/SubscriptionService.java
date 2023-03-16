package org.av360.maverick.eventdispatcher.subscriptions.subscription.application;

import org.av360.maverick.eventdispatcher.shared.dtos.SubscriptionDTO;
import org.av360.maverick.eventdispatcher.subscriptions.grpc.GrpcClientService;
import org.av360.maverick.eventdispatcher.subscriptions.subscription.domain.Subscription;
import org.av360.maverick.eventdispatcher.subscriptions.subscription.domain.SubscriptionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subRepo;

    @Autowired
    private GrpcClientService grpcClientService;

    public Object getGrpc() {
        ArrayList<SubscriptionDTO> subscriptionDTOs = getAllSubscriptions();

        grpcClientService.sendSubscriptions(subscriptionDTOs);
        return null;
    }

    public SubscriptionDTO createSubscription(SubscriptionDTO subscriptionDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Subscription subscription = modelMapper.map(subscriptionDTO, Subscription.class);
        subscription.setId(UUID.randomUUID());
        subscription.setCreatedAt(new Date());

        subRepo.save(subscription);

        SubscriptionDTO returnDTO = modelMapper.map(subscription, SubscriptionDTO.class);

        grpcClientService.sendSubscription(returnDTO);

        return returnDTO;
    }

    public ArrayList<SubscriptionDTO> getAllSubscriptions() {
        ArrayList<Subscription> subscriptions = (ArrayList<Subscription>) subRepo.findAll();
        ModelMapper modelMapper = new ModelMapper();
        ArrayList<SubscriptionDTO> returnDTOs = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            SubscriptionDTO returnDTO = modelMapper.map(subscription, SubscriptionDTO.class);
            returnDTOs.add(returnDTO);
        }
        return returnDTOs;
    }

    public SubscriptionDTO getSubscription(UUID id) {
        Subscription subscription = subRepo.findById(id).orElseThrow();
        ModelMapper modelMapper = new ModelMapper();
        SubscriptionDTO returnDTO = modelMapper.map(subscription, SubscriptionDTO.class);
        return returnDTO;
    }

    public void deleteSubscription(UUID id) {
        subRepo.deleteById(id);
        grpcClientService.deleteSubscription(id);
    }


}
