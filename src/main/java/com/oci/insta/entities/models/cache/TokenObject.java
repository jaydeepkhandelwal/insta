package com.oci.insta.entities.models.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javatuples.Pair;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenObject implements Serializable {
    private String token;
    private Long id;
    private List<Pair<Long,String>> roles;
    @NotNull
    private Date issuedAt;
    @NotNull
    private Date expiresAt;
    private static final long serialVersionUID = -1L;
}
