// src/components/dashboard/EmployeeList.jsx
import React from 'react';

// ... (Icon components remain the same) ...
const ViewIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
    <path fillRule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.022 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clipRule="evenodd" />
  </svg>
);

const EditIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
    <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
  </svg>
);

const DeleteIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
    <path fillRule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clipRule="evenodd" />
  </svg>
);


const EmployeeList = ({ employees, onView, onEdit, onDelete }) => {
  return (
    <div className="overflow-x-auto border border-slate-200 rounded-lg">
      <table className="min-w-full divide-y divide-slate-200">
        <thead className="bg-slate-50">
          <tr>
            <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
              Emp Code
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
              Name
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
              Company Email
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
              Manager
            </th>
            <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
              Current Project
            </th>
            <th className="px-6 py-3 text-right text-xs font-medium text-slate-500 uppercase tracking-wider">
              Actions
            </th>
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-slate-200">
          {employees.map((emp) => (
            <tr key={emp.id} className="hover:bg-slate-50">
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-900">
                {emp.employmentCode} {/* ✨ Use direct field from Employee */}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-700">
                {emp.personalDetails.fullName}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-700">
                {emp.professionalDetails.companyEmail}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-700">
                {emp.managerName} {/* ✨ Use direct field from Employee */}
              </td>
              <td className="px-6 py-4 whitespace-nowrowrap text-sm text-slate-700">
                {/* ✨ MODIFIED THIS LINE */}
                {/* Use the 'currentProjectName' field as per docs */}
                {emp.currentProjectName || '-'}
                {/* ✨ END MODIFICATION */}
              </td>
              <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-right space-x-4">
                <button
                  onClick={() => onView(emp)}
                  className="text-blue-600 hover:text-blue-900"
                  title="View"
                >
                  <ViewIcon />
                </button>
                <button
                  onClick={() => onEdit(emp)}
                  className="text-purple-600 hover:text-purple-900"
                  title="Edit"
                >
                  <EditIcon />
                </button>
                <button
                  onClick={() => onDelete(emp.id)}
                  className="text-red-600 hover:text-red-900"
                  title="Delete"
                >
                  <DeleteIcon />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default EmployeeList;