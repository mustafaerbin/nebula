package com.tr.nebula.persistence.api.criteria.criterion;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Mustafa Erbin on 16/01/2017.
 */
public class RestrictionList extends Restriction {
    private final List<Restriction> restrictions;
    public RestrictionList(Operator operator, Restriction[] restrictions) {
        this(operator, Arrays.asList(restrictions));
    }
    public RestrictionList(Operator operator, List<Restriction> restrictions) {
        super(operator, null, null);
        this.restrictions = restrictions;
    }

    public List<Restriction> getRestrictions() {
        return restrictions;
    }
}
