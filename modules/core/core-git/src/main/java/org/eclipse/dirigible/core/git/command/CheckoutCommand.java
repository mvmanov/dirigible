/**
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.core.git.command;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.eclipse.dirigible.core.git.GitConnectorException;
import org.eclipse.dirigible.core.git.GitConnectorFactory;
import org.eclipse.dirigible.core.git.IGitConnector;
import org.eclipse.dirigible.core.git.project.ProjectPropertiesVerifier;
import org.eclipse.dirigible.core.git.utils.GitFileUtils;
import org.eclipse.dirigible.core.publisher.api.PublisherException;
import org.eclipse.dirigible.core.publisher.service.PublisherCoreService;
import org.eclipse.dirigible.core.workspace.api.IProject;
import org.eclipse.dirigible.core.workspace.api.IWorkspace;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Pull project(s) from a Git repository and optionally publish it.
 */
public class CheckoutCommand {

//	private static final String CHANGES_BRANCH = "changes_branch_"; //$NON-NLS-1$

	private static final Logger logger = LoggerFactory.getLogger(CheckoutCommand.class);

	/** The publisher core service. */
	@Inject
	private PublisherCoreService publisherCoreService;

	/** The project metadata manager. */
//	@Inject
//	private ProjectMetadataManager projectMetadataManager;

	/** The verifier. */
	@Inject
	private ProjectPropertiesVerifier verifier;

	/** The git file utils. */
	@Inject
	private GitFileUtils gitFileUtils;

	/**
	 * Execute a Pull command.
	 *
	 * @param workspace
	 *            the workspace
	 * @param project
	 *            the project
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param branch
	 *            the branch
	 * @param publishAfterPull
	 *            the publish after pull
	 */
	public void execute(final IWorkspace workspace, final IProject project, final String username, final String password, 
			final String branch, final boolean publishAfterPull) {
		String user = UserFacade.getName();
		boolean atLeastOne = false;
		if (verifier.verify(workspace, project)) {
			logger.debug(String.format("Start checkout %s project and %s branch...", project.getName(), branch));
			boolean checkedout = checkoutProjectFromGitRepository(user, workspace, project, username, password, branch);
			atLeastOne = atLeastOne ? atLeastOne : checkedout;
			logger.debug(String.format("Pull of the Project %s finished.", project.getName()));
		} else {
			logger.warn(String.format("Project %s is local only. Select a previously cloned project for Checkout operation.", project));
		}

		if (atLeastOne && publishAfterPull) {
			publishProjects(workspace, Arrays.asList(project));
		}

	}

	/**
	 * Checkout project from git repository by executing several low level Git commands.
	 *
	 * @param workspace
	 *            the workspace
	 * @param selectedProject
	 *            the selected project
	 * @return true, if successful
	 */
	boolean checkoutProjectFromGitRepository(final String user, final IWorkspace workspace, final IProject selectedProject, 
			final String username, final String password, final String branch) {
		final String errorMessage = String.format("Error occurred while pulling project [%s]", selectedProject.getName());
//		GitProjectProperties gitProperties = null;
//		try {
//			gitProperties = gitFileUtils.getGitPropertiesForProject(workspace, selectedProject);
//			if (gitProperties != null) {
//				logger.debug(String.format("Git properties for the project [%s]: %s", selectedProject.getName(), gitProperties.toString()));
//			} else {
//				logger.debug(String.format("Git properties file for the project [%s] is null", selectedProject.getName()));
//				return false;
//			}
//		} catch (IOException e) {
//			logger.error("This is not a git project!", e);
//			return false;
//		}

//		String gitRepositoryURI = gitProperties.getURL();
//		String gitRepositoryBranch = branch != null ? branch : ProjectMetadataManager.BRANCH_MASTER;
//		try {
//			projectMetadataManager.ensureProjectMetadata(workspace, selectedProject.getName(), gitRepositoryURI, gitRepositoryBranch);
//			if (branch == null) {
//				gitRepositoryBranch = ProjectMetadataManager.getBranch(selectedProject);
//				if (gitRepositoryBranch == null) {
//					gitRepositoryBranch = ProjectMetadataManager.BRANCH_MASTER;
//				}
//			}
//			logger.debug(String.format("Repository URL for the project [%s]: %s", selectedProject.getName(), gitRepositoryURI));
//			logger.debug(String.format("Branch for the project [%s]: %s", selectedProject.getName(), gitRepositoryBranch));
//		} catch (IOException e) {
//			logger.error("Error during pull!", e);
//		}

//		File tempGitDirectory = null;
		try {
//			String repositoryName = GitFileUtils.generateGitRepositoryName(gitRepositoryURI); //gitRepositoryURI.substring(gitRepositoryURI.lastIndexOf(SLASH) + 1, gitRepositoryURI.lastIndexOf(DOT_GIT));
//			tempGitDirectory = GitFileUtils.createGitDirectory(repositoryName);
//			logger.debug(
//					String.format("Temp Git Directory for the project [%s]: %s", selectedProject.getName(), tempGitDirectory.getCanonicalPath()));
//
//			logger.debug(String.format("Cloning repository %s, with username %s for branch %s in the directory %s ...", gitRepositoryURI, "[nobody]",
//					gitRepositoryBranch, tempGitDirectory.getCanonicalPath()));
//			GitConnectorFactory.cloneRepository(tempGitDirectory.getCanonicalPath(), gitRepositoryURI, username, password, gitRepositoryBranch);
//			logger.debug(String.format("Cloning repository %s finished.", gitRepositoryURI));
//
			String gitDirectoryPath = gitFileUtils.getAbsolutePath(selectedProject.getPath());
			File gitDirectory = new File(gitDirectoryPath).getCanonicalFile();
			IGitConnector gitConnector = GitConnectorFactory.getConnector(gitDirectory.getCanonicalPath());
//
//			String lastSHA = gitProperties.getSHA();
//
//			String lastSHAForBranch = gitConnector.getLastSHAForBranch(gitRepositoryBranch);
//			gitProperties.setSHA(lastSHAForBranch);
//
//			final String changesBranch = CHANGES_BRANCH + System.currentTimeMillis() + "_" + UserFacade.getName();
//			logger.debug(String.format("Last SHA for the project [%s]: %s", selectedProject.getName(), lastSHA));
//			logger.debug(String.format("'Changes' branch for the project [%s]: %s", selectedProject.getName(), changesBranch));
//
//			logger.debug(String.format("Staring checkout of the project [%s] for the branch %s...", selectedProject.getName(), gitRepositoryBranch));
//			try {
//				gitConnector.checkout(lastSHA);
//			} catch (InvalidRefNameException e) {
//				lastSHA = "HEAD";
//			}
//			logger.debug(String.format("Checkout of the project [%s] finished.", selectedProject.getName()));
//
//			gitConnector.createBranch(changesBranch, lastSHA);
//
//			logger.debug(String.format("Staring checkout of the project [%s] for the branch %s...", selectedProject.getName(), changesBranch));
//			gitConnector.checkout(changesBranch);
//			logger.debug(String.format("Checkout of the project [%s] finished.", selectedProject.getName()));
//
//			logger.debug(String.format("Clean and copy the sources of the project [%s] in directory %s...", selectedProject.getName(),
//					tempGitDirectory.getCanonicalPath()));
//			GitFileUtils.deleteProjectFolderFromDirectory(tempGitDirectory, selectedProject.getName());
//			GitFileUtils.copyProjectToDirectory(selectedProject, tempGitDirectory);
//			logger.debug(String.format("Clean and copy the sources of the project [%s] finished.", selectedProject.getName()));
//
//			gitConnector.add(IGitConnector.GIT_ADD_ALL_FILE_PATTERN);
//			gitConnector.commit("", "", "", true); //$NON-NLS-1$
//			logger.debug(String.format("Commit changes for the project [%s] finished.", selectedProject.getName()));

//			String gitRepositoryBranch = gitConnector.getBranch();
			logger.debug(String.format("Starting checkout of the project [%s] and branch %s ...", selectedProject.getName(), branch));
			gitConnector.checkout(branch);
			logger.debug(String.format("Checkout of the project %s and branch %s finished.", selectedProject.getName(), branch));

			int numberOfConflictingFiles = gitConnector.status().getConflicting().size();
			logger.debug(String.format("Number of conflicting files in the project [%s]: %d.", selectedProject.getName(), numberOfConflictingFiles));
//			if (numberOfConflictingFiles == 0) {
//				logger.debug(String.format("No conflicting files in the project [%s]. Staring checkout and rebase...", selectedProject.getName()));
//				gitConnector.checkout(gitRepositoryBranch);
//				logger.debug(String.format("Checkout for the project [%s] finished.", selectedProject.getName()));
//				gitConnector.rebase(changesBranch);
//				logger.debug(String.format("Rebase for the project [%s] finished.", selectedProject.getName()));
//
//				String dirigibleUser = UserFacade.getName();
//
//				gitFileUtils.deleteRepositoryProject(selectedProject);
//
//				String workspacePath = GitProjectProperties.generateWorkspacePath(workspace, dirigibleUser);
//
//				logger.debug(String.format("Starting importing projects from the Git directory %s.", tempGitDirectory.getCanonicalPath()));
//				gitFileUtils.importProject(tempGitDirectory, workspacePath, dirigibleUser, workspace.getName(), gitProperties, null);
//				logger.debug(String.format("Importing projects from the Git directory %s finished.", tempGitDirectory.getCanonicalPath()));
//			} else {
//				String message = String.format(
//						"Project [%s] has %d conflicting file(s). You can use Push to submit your changes in a new branch for further merge or use Reset to abandon your changes.",
//						selectedProject.getName(), numberOfConflictingFiles);
//				logger.error(message);
//			}
			
			if (numberOfConflictingFiles > 0) {
				String message = String.format(
					"Project [%s] has %d conflicting file(s). You can use Push to submit your changes in a new branch for further merge or use Reset to abandon your changes.",
					selectedProject.getName(), numberOfConflictingFiles);
				logger.error(message);
			}
		} catch (CheckoutConflictException e) {
			logger.error(errorMessage, e);
		} catch (IOException e) {
			logger.error(errorMessage, e);
		} catch (InvalidRemoteException e) {
			logger.error(errorMessage, e);
		} catch (TransportException e) {
			logger.error(errorMessage, e);
			Throwable rootCause = e.getCause();
			if (rootCause != null) {
				rootCause = rootCause.getCause();
				logger.error(errorMessage, e);
				if (rootCause instanceof UnknownHostException) {
					logger.error("Please check if proxy settings are set properly");
				} else {
					logger.error("Doublecheck the correctness of the [Username] and/or [Password] or [Git Repository URI]");
				}
			}
		} catch (GitAPIException e) {
			logger.error(errorMessage, e);
		} catch (GitConnectorException e) {
			logger.error(errorMessage, e);
		} finally {
//			try {
//				GitFileUtils.deleteDirectory(tempGitDirectory);
//			} catch (IOException e) {
//				logger.error(e.getMessage(), e);
//			}
		}
		return true;
	}

	/**
	 * Publish projects.
	 *
	 * @param workspace
	 *            the workspace
	 * @param pulledProjects
	 *            the pulled projects
	 */
	protected void publishProjects(IWorkspace workspace, List<IProject> pulledProjects) {
		if (pulledProjects.size() > 0) {
			for (IProject pulledProject : pulledProjects) {
				List<IProject> projects = workspace.getProjects();
				for (IProject project : projects) {
					if (project.getName().equals(pulledProject.getName())) {
						try {
							publisherCoreService.createPublishRequest(workspace.getName(), pulledProject.getName());
							logger.info(String.format("Project [%s] has been published", project.getName()));
						} catch (PublisherException e) {
							logger.error(String.format("An error occurred while publishing the pulled project [%s]", project.getName()), e);
						}
						break;
					}
				}
			}
		}
	}

}