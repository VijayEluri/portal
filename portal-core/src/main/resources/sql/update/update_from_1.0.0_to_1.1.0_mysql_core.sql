UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.forgotemail'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.forgotemail';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.reconfirmemail'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.reconfirmemail';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.contactformemail'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.contactformemail';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.regemail'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.regemail';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.registereduser'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.registereduser';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.unknownerror'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.unknownerror';
UPDATE core_configuration set conf_key = 'spring.emailService.findAll.subject.id.forgotemail'
  WHERE conf_key = 'spring.emailTemplateDao.findAll.subject.id.forgotemail';
UPDATE core_configuration set conf_key = 'spring.roleService.findAll.description.id.guestrole'
  WHERE conf_key = 'spring.roleDao.findAll.description.id.guestrole';
UPDATE core_configuration set conf_key = 'spring.roleService.findAll.description.id.registerrole'
  WHERE conf_key = 'spring.roleDao.findAll.description.id.registerrole';


SET FOREIGN_KEY_CHECKS=0;
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.GlobalAdminBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.GlobalAdminBoxPanel';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.LoginBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.LoginBoxPanel';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.OtherBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.OtherBoxPanel';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.PageAdminBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.PageAdminBoxPanel';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.TagCloudBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.TagCloudBoxPanel';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'general.UserBoxPanel';
DELETE FROM core_right WHERE right_id LIKE 'general.UserBoxPanel';

UPDATE core_role_right_xref SET right_id = 'box.admin' WHERE right_id LIKE 'page.BoxPage';
UPDATE core_right SET right_id = 'box.admin', description = 'Box Administration' WHERE right_id LIKE 'page.BoxPage';
UPDATE core_role_right_xref SET right_id = 'configuration.admin' WHERE right_id LIKE 'page.ConfigurationPage';
UPDATE core_right SET right_id = 'configuration.admin', description = 'Configuration Administration' WHERE right_id LIKE 'page.ContactPage';
UPDATE core_role_right_xref SET right_id = 'contact' WHERE right_id LIKE 'page.ConfigurationPage';
UPDATE core_right SET right_id = 'contact', description = 'Contact Form: User who are allowed to access the contact form' WHERE right_id LIKE 'page.ContactPage';
UPDATE core_role_right_xref SET right_id = 'emailtemplate.admin' WHERE right_id LIKE 'page.EmailTemplatePage';
UPDATE core_right SET right_id = 'emailtemplate.admin', description = 'Email Templates Administration' WHERE right_id LIKE 'page.EmailTemplatePage';
UPDATE core_role_right_xref SET right_id = 'right.admin' WHERE right_id LIKE 'page.RightPage';
UPDATE core_right SET right_id = 'right.admin', description = 'Rights Administration' WHERE right_id LIKE 'page.RightPage';
UPDATE core_role_right_xref SET right_id = 'role.admin' WHERE right_id LIKE 'page.RolePage';
UPDATE core_right SET right_id = 'role.admin', description = 'Roles Administration' WHERE right_id LIKE 'page.RolePage';
UPDATE core_role_right_xref SET right_id = 'user.admin' WHERE right_id LIKE 'page.UserPage';
UPDATE core_right SET right_id = 'user.admin', description = 'User Administration' WHERE right_id LIKE 'page.UserPage';
UPDATE core_role_right_xref SET right_id = 'theme.admin' WHERE right_id LIKE 'page.ThemePage';
UPDATE core_right SET right_id = 'theme.admin', description = 'Theme Administration' WHERE right_id LIKE 'page.ThemePage';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'page.UploadThemePage';
DELETE FROM core_right WHERE right_id LIKE 'page.UploadThemePage';
UPDATE core_role_right_xref SET right_id = 'modulemgmt.admin' WHERE right_id LIKE 'page.ModuleOverviewPage';
UPDATE core_right SET right_id = 'modulemgmt.admin', description = 'Module Administration' WHERE right_id LIKE 'page.ModuleOverviewPage';
DELETE FROM core_role_right_xref WHERE right_id LIKE 'page.ModuleLinkPage';
DELETE FROM core_right WHERE right_id LIKE 'page.ModuleLinkPage';
SET FOREIGN_KEY_CHECKS=1;
