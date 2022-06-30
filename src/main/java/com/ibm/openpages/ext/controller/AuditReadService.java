package com.ibm.openpages.ext.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.openpages.api.metadata.Id;
import com.ibm.openpages.api.query.ITabularResultSet;
import com.ibm.openpages.api.resource.IGRCObject;
import com.ibm.openpages.ext.constant.AuditScopingHelperConstant;
import com.ibm.openpages.ext.helper.QueryExecutor;
import com.ibm.openpages.ext.helper.transformer.IDNameDescriptionMapper;
import com.ibm.openpages.ext.helper.transformer.IDNameDescriptionSrcRefMapper;
import com.ibm.openpages.ext.helper.transformer.IssueAuditableEntityMapper;
import com.ibm.openpages.ext.helper.transformer.L4IssueMapper;
import com.ibm.openpages.ext.model.OPObject;
import com.ibm.openpages.ext.tss.service.IFieldUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectTypeUtil;
import com.ibm.openpages.ext.tss.service.IGRCObjectUtil;
import com.ibm.openpages.ext.tss.service.ILoggerUtil;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/jspview/auditScopingApp")
public class AuditReadService {

}