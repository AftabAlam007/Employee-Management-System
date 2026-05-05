// src/components/dashboard/AdminDashboard.jsx
import React, { useState, useEffect } from 'react';
import EmployeeList from './EmployeeList';
import EmployeeForm from './EmployeeForm';
import Modal from '../common/Modal';
import {
  PersonalDetailsView,
  ProfessionalDetailsView,
  ProjectHistoryView,
  FinanceView,
} from './ViewOnlyDetails';
import { getAllEmployees, createEmployee, updateEmployee, deleteEmployee } from '../../services/employeeService';

const AdminDashboard = () => {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [modalState, setModalState] = useState({
    isOpen: false,
    mode: null, // 'add', 'edit', 'view'
    data: null, // The employee data for 'edit' or 'view'
  });

  useEffect(() => {
    fetchEmployees();
  }, []);

  const fetchEmployees = async () => {
    try {
      setLoading(true);
      const data = await getAllEmployees();
      setEmployees(data);
    } catch (err) {
      setError('Failed to fetch employees.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenAddModal = () => {
    setModalState({ isOpen: true, mode: 'add', data: null });
  };

  const handleOpenEditModal = (employee) => {
    setModalState({ isOpen: true, mode: 'edit', data: employee });
  };

  const handleOpenViewModal = (employee) => {
    setModalState({ isOpen: true, mode: 'view', data: employee });
  };

  const handleCloseModal = () => {
    setModalState({ isOpen: false, mode: null, data: null });
  };

  const handleDeleteEmployee = async (id) => {
    if (window.confirm('Are you sure you want to delete this employee?')) {
      try {
        await deleteEmployee(id);
        fetchEmployees(); // Refresh the list
      } catch (err) {
        setError('Failed to delete employee.');
        console.error(err);
      }
    }
  };

  const handleSaveEmployee = async (formData) => {
    try {
      if (modalState.mode === 'edit') {
        await updateEmployee(modalState.data.id, formData);
      } else {
        await createEmployee(formData);
      }
      fetchEmployees(); // Refresh the list
      handleCloseModal();
    } catch (err) {
      setError('Failed to save employee.');
      console.error(err);
    }
  };

  const getModalTitle = () => {
    if (modalState.mode === 'add') return 'Add New Employee';
    if (modalState.mode === 'edit') return 'Edit Employee';
    if (modalState.mode === 'view' && modalState.data && modalState.data.personalDetails) return `Viewing ${modalState.data.personalDetails.fullName}`;
    return '';
  };

  if (loading) {
    return <div className="text-center py-8">Loading employees...</div>;
  }

  if (error) {
    return <div className="text-center py-8 text-red-600">{error}</div>;
  }

  return (
    <div className="bg-white p-8 rounded-xl shadow-lg max-w-7xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold text-slate-800">Employee List</h2>
        <button
          onClick={handleOpenAddModal}
          className="bg-purple-600 text-white px-5 py-2 rounded-lg font-medium hover:bg-purple-700 transition-colors shadow-sm flex items-center gap-2"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
            <path fillRule="evenodd" d="M10 3a1 1 0 011 1v5h5a1 1 0 110 2h-5v5a1 1 0 11-2 0v-5H4a1 1 0 110-2h5V4a1 1 0 011-1z" clipRule="evenodd" />
          </svg>
          Add Employee
        </button>
      </div>

      <EmployeeList
        employees={employees}
        onView={handleOpenViewModal}
        onEdit={handleOpenEditModal}
        onDelete={handleDeleteEmployee}
      />

      <Modal
        isOpen={modalState.isOpen}
        onClose={handleCloseModal}
        title={getModalTitle()}
        size={modalState.mode === 'view' ? 'max-w-4xl' : 'max-w-6xl'}
      >
        {(modalState.mode === 'add' || modalState.mode === 'edit') && (
          <EmployeeForm
            employeeToEdit={modalState.data}
            onSave={handleSaveEmployee}
            onCancel={handleCloseModal}
          />
        )}
        {modalState.mode === 'view' && modalState.data && (
          <div className="space-y-8">
            <PersonalDetailsView data={modalState.data.personalDetails} />
            <ProfessionalDetailsView data={modalState.data.professionalDetails} />
            {/* Assuming projectDetails and financeDetails are directly on employee object or handled within views */}
            <ProjectHistoryView data={modalState.data.projects} />
            <FinanceView data={modalState.data.finance} />
          </div>
        )}
      </Modal>
    </div>
  );
};

export default AdminDashboard;