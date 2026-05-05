// src/services/employeeService.js
import apiClient from '../api/apiClient';

export const createEmployee = async (employeeData) => {
    const response = await apiClient.post('/employees', employeeData);
    return response.data;
};

export const getAllEmployees = async () => {
    const response = await apiClient.get('/employees');
    return response.data;
};

export const getEmployeeById = async (id) => {
    const response = await apiClient.get(`/employees/${id}`);
    return response.data;
};

export const getEmployeeByEmail = async (email) => {
    const response = await apiClient.get(`/employees/by-email?email=${email}`);
    return response.data;
};

export const updateEmployee = async (id, employeeData) => {
    const response = await apiClient.put(`/employees/${id}`, employeeData);
    return response.data;
};

export const deleteEmployee = async (id) => {
    const response = await apiClient.delete(`/employees/${id}`);
    return response.data;
};

export const downloadPayslip = async (id) => {
    const response = await apiClient.get(`/employees/${id}/payslip`, {
        responseType: 'blob', // Important for file downloads
    });
    return response.data;
};

