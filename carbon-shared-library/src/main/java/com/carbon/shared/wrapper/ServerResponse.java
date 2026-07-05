package com.carbon.shared.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponse<R> {

    private String serviceId;
    private long timestamp;
    private R response;

    public ServerResponse<R> of(R response) {
        return new ServerResponse<>(serviceId, timestamp, response);
    }
}
