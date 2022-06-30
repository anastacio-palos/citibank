import {IHeaderInfo} from "../../components/header/CustomHeader";

export interface AuditableEntityInitModel {
    objectID: string;
    objectType: string;
    objectStatus?: null;
    objectTypeLabel: string;
    objRegistrySetting: string;
    dsmtSearchInfo?: null;
    helperObjectContentInfo: HelperObjectContentInfo;
    helperHeaderInfo?: null;
    headerInfo: IHeaderInfo;
    auditableEntityNodeLevelInfo: AuditableEntityNodeLevelInfo;
    dsmtLinkUpdateInfo?: null;
    newQuadsInfo?: null;
    existingQuadsInfo?: null;
    availableQuadsInfo?: null;
    associateNewDSMTInfoList?: null;
    existingDSMTLinksGridInfo: ExistingDSMTLinksGridInfo;
    newDSMTLinksGridInfo?: null;
    availableDSMTLinksGridInfo?: null;
}

export interface HelperObjectContentInfo {
    contentBody: string;
    contentHeader: string;
    generalDetails?: (GeneralDetailsEntity)[] | null;
}

export interface GeneralDetailsEntity {
    fieldName: string;
    fieldValue: string;
    editable: boolean;
    required: boolean;
}

// export interface HeaderInfo {
//   headerTitle: string;
//   userInfo: UserInfo;
//   aboutInfo: AboutInfo;
//   hideSearch: boolean;
//   hideUserIcon: boolean;
//   hideAppSwitcher: boolean;
//   hidNotificationBell: boolean;
// }
export interface UserInfo {
    displayName: string;
    displayImage?: any;
}

export interface AboutInfo {
    title: string;
    version: string;
    licenseInfo: string;
    helperInfo: string;
}

export interface AuditableEntityNodeLevelInfo {
    managdSegNodeLevels?: null;
    manageGeoNodeLevels?: null;
}

export interface ExistingDSMTLinksGridInfo {
    rows?: (RowsEntity)[] | null;
    headers?: (HeadersEntity)[] | null;
}

export interface RowsEntity {
    hasChildren: boolean;
    id: string;
    disableInfo?: null;
    name: string;
    type?: null;
    link: string;
    scope?: string | null;
    status?: null;
    baseId?: null;
    active?: null;
    description?: null;
    resourceId?: null;
    children?: null;
    expanded: boolean;
    selected: boolean;
    disabled: boolean;
}

export interface HeadersEntity {
    key: string;
    header: string;
}
