// src/components/dashboard/ViewOnlyDetails.jsx
import React from 'react';

// Reusable item for displaying a key-value pair
const DetailItem = ({ label, value }) => (
  <div className="py-2 sm:grid sm:grid-cols-3 sm:gap-4">
    <dt className="text-sm font-medium text-slate-500">{label}</dt>
    <dd className="mt-1 text-sm text-slate-900 sm:mt-0 sm:col-span-2">
      {value || '-'}
    </dd>
  </div>
);

// Reusable card for each section
const DetailCard = ({ title, children }) => (
  <div className="bg-white p-8 rounded-xl shadow-lg">
    <h3 className="text-2xl font-semibold text-purple-600 mb-6 border-b pb-3">
      {title}
    </h3>
    <dl className="divide-y divide-slate-200">{children}</dl>
  </div>
);

export const PersonalDetailsView = ({ data }) => (
  <DetailCard title="Personal Details">
    <DetailItem label="Full Name" value={data.fullName} />
    <DetailItem label="Date of Birth" value={data.dateOfBirth} />
    <DetailItem label="Gender" value={data.gender} />
    <DetailItem label="Age" value={data.age} />
    <DetailItem label="Mobile" value={data.mobile} />
    <DetailItem label="Personal Email" value={data.personalEmail} />
    <DetailItem
      label="Current Address"
      value={`${data.currentAddress.addressLine1}, ${data.currentAddress.city} - ${data.currentAddress.pinCode}`}
    />
    <DetailItem
      label="Permanent Address"
      value={`${data.permanentAddress.addressLine1}, ${data.permanentAddress.city} - ${data.permanentAddress.pinCode}`}
    />
    <DetailItem
      label="Emergency Contact"
      value={`${data.emergencyContactName} (${data.emergencyContactMobile})`}
    />
  </DetailCard>
);

export const ProfessionalDetailsView = ({ data }) => (
  <DetailCard title="Professional Details">
    <DetailItem label="Employment Code" value={data.employmentCode} />
    <DetailItem label="Company Email" value={data.companyEmail} />
    <DetailItem label="Date of Joining" value={data.dateOfJoining} />
    <DetailItem label="Reporting Manager" value={data.reportingManagerEmployeeCode} />
    <DetailItem label="HR Name" value={data.hrName} />
    <DetailItem label="Office Phone" value={data.officePhone} />
    <DetailItem
      label="Office Address"
      value={`${data.officeAddress.addressLine1}, ${data.officeAddress.city} - ${data.officeAddress.pinCode}`}
    />
    {/* Employment History Table */}
    <div className="py-4">
      <dt className="text-sm font-medium text-slate-500 mb-2">
        Employment History
      </dt>
      <dd className="mt-1 text-sm text-slate-900 sm:mt-0 sm:col-span-2">
        {data.employmentHistory.length > 0 ? (
          <ul className="list-disc pl-5 space-y-1">
            {data.employmentHistory.map((job, idx) => (
              <li key={idx}>
                <strong>{job.companyName}</strong> ({job.joiningDate} to{' '}
                {job.endDate})
              </li>
            ))}
          </ul>
        ) : (
          '-'
        )}
      </dd>
    </div>
  </DetailCard>
);

export const ProjectHistoryView = ({ data }) => (
  <DetailCard title="Project History">
    {data.length > 0 ? (
      data.map((project, idx) => (
        <div key={idx} className="py-4">
          <DetailItem label="Project Name" value={project.clientOrProjectName} />
          <DetailItem label="Project Code" value={project.projectCode} />
          <DetailItem
            label="Duration"
            value={`${project.startDate} to ${project.endDate}`}
          />
          <DetailItem
            label="Reporting Manager"
            value={project.reportingManagerEmployeeCode}
          />
        </div>
      ))
    ) : (
      <p className="text-sm text-slate-700">No project history available.</p>
    )}
  </DetailCard>
);

export const FinanceView = ({ data }) => (
  <DetailCard title="Finance Details">
    <DetailItem label="PAN Card" value={data.panCard} />
    <DetailItem label="Aadhar Card" value={data.aadharCard} />
    <DetailItem label="Bank Name" value={data.bankDetails.bankName} />
    <DetailItem label="Branch" value={data.bankDetails.branch} />
    <DetailItem label="IFSC Code" value={data.bankDetails.ifscCode} />
    <DetailItem label="CTC Breakup" value={data.ctcBreakup} />
  </DetailCard>
);

const ViewOnlyDetails = ({ employee }) => {
    if (!employee) return null;

    return (
        <div className="max-h-[80vh] overflow-y-auto p-1">
            <h2 className="text-3xl font-bold text-slate-800 mb-6">
                Employee Details
            </h2>
            <div className="space-y-8">
                <PersonalDetailsView data={employee.personalDetails} />
                <ProfessionalDetailsView data={employee.professionalDetails} />
                <ProjectHistoryView data={employee.projects} />
                <FinanceView data={employee.finance} />
            </div>
        </div>
    );
};

export default ViewOnlyDetails;