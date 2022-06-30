import React, {useEffect, useState} from 'react';
import Search20 from "@carbon/icons-react/lib/search/20";
import User20 from "@carbon/icons-react/lib/user/20";
import Notification20 from "@carbon/icons-react/lib/notification/20";
import AppSwitcher20 from "@carbon/icons-react/lib/app-switcher/20";
import {
    Header,
    HeaderName,
    HeaderGlobalAction,
    HeaderGlobalBar,
    HeaderNavigation,
    HeaderMenuItem,
    HeaderPanel,
    Switcher,
    SwitcherItem
} from "carbon-components-react/lib/components/UIShell";
import axios from 'axios';
import {Help20} from '@carbon/icons-react';
import {RootStoreProvider, StoreTypes, useStore} from '../../providers/RootStoreProvider';
import {CustomHeaderDataStore} from '../../stores/customHeader/customHeaderDataStore';
import {observer} from 'mobx-react-lite';
import {HelperNames} from '../nestingTable/mockApi/mockApi';
import {Modal} from 'carbon-components-react';

export interface IUserInfo {
    displayName: string;
    displayImage?: any
}

export interface IAboutInfo {
    title: string;
    version: string;
    licenseInfo: string;
    helperInfo: string;
}

export interface IHeaderInfo {
    headerTitle: string;
    hideSearch: boolean;
    hideNotificationBell: boolean;
    hideAppSwitcher: boolean;
    hideUserIcon: boolean;
    userInfo: IUserInfo;
    aboutInfo: IAboutInfo;
}

export interface ICustomHeaderProps {
    helperName: HelperNames;
    url: string;
}

interface ISidePanelItemProps {
    itemText: string[];
}

const CustomHeader: React.FC<ICustomHeaderProps> = observer((props) => {
    const {
        headerTitle,
        hideSearch,
        hideNotificationBell,
        hideAppSwitcher,
        hideUserIcon,
        userInfo,
        aboutInfo
    } = useStore(StoreTypes.CustomHeaderStore) as CustomHeaderDataStore;

    const [data, setData] = useState({});
    const [isUserPanelExpanded, setIsUserPanelExpanded] = useState<boolean>(false);
    const [isHelpPanelExpanded, setIsHelpPanelExpanded] = useState<boolean>(false);
    const [isAboutModalOpen, setIsAboutModalOpen] = useState<boolean>(false);

    const handleUserIconClick = () => {
        setIsUserPanelExpanded(!isUserPanelExpanded);
    };

    const handleHelpIconClick = () => {
        setIsHelpPanelExpanded(!isHelpPanelExpanded);
    };

    const handleAboutModalClick = () => {
        setIsAboutModalOpen(!isAboutModalOpen);
    }

    return (
        <RootStoreProvider>
            <AboutModal
                isOpen={isAboutModalOpen}
                setOpenState={setIsAboutModalOpen}
                aboutInfo={aboutInfo}/>
            <Header aria-label="IBM OpenPages with Watson">
                <HeaderName href="#" prefix="">
                    IBM OpenPages with Watson
                </HeaderName>
                <HeaderNavigation aria-label={headerTitle}>
                    <HeaderMenuItem href="#">
                        {headerTitle}
                    </HeaderMenuItem>
                </HeaderNavigation>
                <HeaderGlobalBar>
                    {
                        !hideSearch &&
                        <HeaderGlobalAction aria-label="Search" onClick={() => {
                        }}>
                            <Search20/>
                        </HeaderGlobalAction>
                    }
                    {
                        !hideNotificationBell &&
                        <HeaderGlobalAction aria-label="View Notifications" onClick={() => {
                        }}>
                            <Notification20/>
                        </HeaderGlobalAction>
                    }
                    {
                        !hideUserIcon &&
                        <HeaderGlobalAction aria-label="Open User Menu" onClick={() => handleUserIconClick()}>
                            <User20/>
                            <HeaderPanel aria-label="User Panel" expanded={isUserPanelExpanded}>
                                <SidePanelItem itemText={[userInfo.displayName]}/>
                            </HeaderPanel>
                        </HeaderGlobalAction>
                    }
                    {
                        !hideAppSwitcher &&
                        <HeaderGlobalAction aria-label="App Switcher" onClick={() => {
                        }}>
                            <AppSwitcher20/>
                        </HeaderGlobalAction>
                    }
                    <HeaderGlobalAction aria-label="Help" onClick={() => handleHelpIconClick()}>
                        <Help20/>
                        <HeaderPanel aria-label="Help Panel" expanded={isHelpPanelExpanded}>
                            <Switcher aria-label="Switcher">
                                <SwitcherItem aria-label="Switcher Item">Help</SwitcherItem>
                                <SwitcherItem
                                    aria-label="Switcher Item"
                                    onClick={() => handleAboutModalClick()}>
                                    About
                                </SwitcherItem>
                            </Switcher>
                        </HeaderPanel>
                    </HeaderGlobalAction>
                </HeaderGlobalBar>
            </Header>
        </RootStoreProvider>
    );
});

const SidePanelItem = ({itemText}: ISidePanelItemProps) => {

    return (
        <Switcher aria-label="Switcher">
            {itemText.map((item, index) => (
                <SwitcherItem aria-label="Switcher Item" key={index}>{item}</SwitcherItem>
            ))}
        </Switcher>
    );
};

interface IAboutModalProps {
    isOpen: boolean;
    setOpenState: any;
    aboutInfo: IAboutInfo
}

const AboutModal = ({isOpen, setOpenState, aboutInfo}: IAboutModalProps) => {
    const handleRequestCloseClick = () => {
        setOpenState(!isOpen);
    }

    return (
        <Modal
            id="about-modal"
            open={isOpen}
            onRequestClose={() => handleRequestCloseClick()}
            passiveModal
            modalHeading={
                <p>About {aboutInfo.title}</p>
            }>
            {aboutInfo.title} <br/>
            {aboutInfo.version} <br/>
            {aboutInfo.helperInfo} <br/>
            {aboutInfo.licenseInfo}
        </Modal>
    );
};

export default CustomHeader;
