// src/components/dashboard/EmployeeForm.jsx
import React, { useState, useEffect } from 'react';
import { createEmployee, updateEmployee } from '../../services/employeeService';
import { toast } from 'react-toastify';

// --- Helper Components (FormInput, AddressFields) ---
const FormInput = ({ label, id, ...props }) => {
    const inputId = id || props.name;
    return (
        <div className="col-span-12 sm:col-span-6 md:col-span-4">
            <label htmlFor={inputId} className="block text-sm font-medium text-slate-700">{label}</label>
            <input id={inputId} {...props} className="mt-1 block w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-purple-500 focus:border-purple-500 disabled:bg-slate-100" />
        </div>
    );
};

const AddressFields = ({ legend, data, onChange, namePrefix }) => (
    <fieldset className="col-span-12 md:col-span-6 grid grid-cols-12 gap-4 border p-4 rounded-md">
        <legend className="text-sm font-medium text-slate-800 px-1">{legend}</legend>
        <FormInput label="Address Line 1" name={`${namePrefix}.addressLine1`} value={data?.addressLine1 || ''} onChange={onChange} required />
        <FormInput label="Address Line 2" name={`${namePrefix}.addressLine2`} value={data?.addressLine2 || ''} onChange={onChange} />
        <FormInput label="City" name={`${namePrefix}.city`} value={data?.city || ''} onChange={onChange} required />
        <FormInput label="Pin Code" name={`${namePrefix}.pinCode`} value={data?.pinCode || ''} onChange={onChange} pattern="\d{6}" title="6 digits" required />
    </fieldset>
);
// --- End Helper Components ---

const BLANK_FORM_STATE = {

    user: { email: '', password: '', role: 'ROLE_EMPLOYEE' },

    managerName: '',

    currentProjectName: '',

    personalDetails: {

        fullName: '', dateOfBirth: '', gender: '', age: '', mobile: '', personalEmail: '',

        emergencyContactName: '', emergencyContactMobile: '',

        currentAddress: { city: '', addressLine1: '', addressLine2: '', pinCode: '' },

        permanentAddress: { city: '', addressLine1: '', addressLine2: '', pinCode: '' },

    },

    professionalDetails: {

        employmentCode: '',

        companyEmail: '', officePhone: '', city: '', dateOfJoining: '',

        reportingManagerEmployeeCode: '', hrName: '',

        officeAddress: { city: '', addressLine1: '', addressLine2: '', pinCode: '' },

        employmentHistory: [],

    },

    projects: [],

    finance: {

        panCard: '', aadharCard: '', ctcBreakup: '',

        bankDetails: { bankName: '', branch: '', ifscCode: '' },

    },

};

const DEV_PREFILLED_STATE = {
    user: { email: 'new.employee@example.com', password: 'password123', role: 'ROLE_EMPLOYEE' },
    managerName: 'Robert Downy',
    currentProjectName: 'Project Iron',
    personalDetails: {
        fullName: 'Tony Stark',
        dateOfBirth: '1985-04-20',
        gender: 'Male',
        age: 39,
        mobile: '1234567890',
        personalEmail: 'tony.stark@personal.com',
        emergencyContactName: 'Pepper Potts',
        emergencyContactMobile: '0987654321',
        currentAddress: {
            addressLine1: '10880 Malibu Point',
            addressLine2: '',
            city: 'Malibu',
            pinCode: '902650',
        },
        permanentAddress: {
            addressLine1: 'Stark Tower',
            addressLine2: '5th Avenue',
            city: 'New York',
            pinCode: '10001',
        },
    },
    professionalDetails: {
        employmentCode: '654321',
        companyEmail: 'tony.stark@starkindustries.com',
        officePhone: '1231231234',
        city: 'New York',
        dateOfJoining: '2010-01-01',
        reportingManagerEmployeeCode: 'NICKFURY01',
        hrName: 'Happy Hogan',
        officeAddress: {
            addressLine1: 'Stark Tower',
            addressLine2: '5th Avenue',
            pinCode: '10001',
            city: 'New York'
        },
        employmentHistory: [],
    },
    projects: [
        {
            projectCode: 'P007',
            startDate: '2022-01-01',
            endDate: '2022-12-31',
            clientOrProjectName: 'US Government',
            reportingManagerEmployeeCode: 'NICKFURY01'
        }
    ],
    finance: {
        panCard: 'STARK1234P',
        aadharCard: '987654321098',
        ctcBreakup: 'All of it',
        bankDetails: {
            bankName: 'Bank of America',
            branch: 'New York',
            ifscCode: 'BOFA0NY',
        },
    },
};

const initialState = import.meta.env.DEV ? DEV_PREFILLED_STATE : BLANK_FORM_STATE;

const EmployeeForm = ({ employee, onSuccess, onClose }) => {
    const [formData, setFormData] = useState(initialState);
    const [error, setError] = useState('');
    const isEditMode = !!employee;

    useEffect(() => {
        if (isEditMode) {
            // Deep copy and ensure nested structures are not null
            const sanitizedEmployee = JSON.parse(JSON.stringify(employee));
            setFormData({
                ...initialState,
                ...sanitizedEmployee,
                user: sanitizedEmployee.user || initialState.user,
                personalDetails: { ...initialState.personalDetails, ...sanitizedEmployee.personalDetails },
                professionalDetails: { ...initialState.professionalDetails, ...sanitizedEmployee.professionalDetails },
                finance: { ...initialState.finance, ...sanitizedEmployee.finance },
            });
        } else {
            setFormData(initialState);
        }
    }, [employee, isEditMode]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        const nameParts = name.split('.');

        setFormData(prev => {
            const newFormData = JSON.parse(JSON.stringify(prev));

            // Special handling for the projects array
            if (nameParts[0] === 'projects' && !newFormData.projects[nameParts[1]]) {
                newFormData.projects[nameParts[1]] = {};
            }

            let current = newFormData;
            for (let i = 0; i < nameParts.length - 1; i++) {
                current = current[nameParts[i]];
            }
            current[nameParts[nameParts.length - 1]] = value;
            return newFormData;
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        try {
            if (isEditMode) {
                await updateEmployee(employee.id, formData);
                toast.success('Employee updated successfully!');
            } else {
                await createEmployee(formData);
                toast.success('Employee created successfully!');
            }
            onSuccess(); // Notify parent component (AdminDashboardPage)
        } catch (err) {
            const errorMsg = err.response?.data?.message || `Failed to ${isEditMode ? 'update' : 'create'} employee.`;
            setError(errorMsg);
            console.error(err);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-8 max-h-[80vh] overflow-y-auto p-2">
            <h2 className="text-2xl font-bold text-slate-800">{isEditMode ? 'Edit Employee' : 'Create New Employee'}</h2>

            {/* --- Top Level Details --- */}
            <fieldset className="grid grid-cols-12 gap-6">
                <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">Core Details</legend>
                <FormInput label="Manager Name" name="managerName" value={formData.managerName || ''} onChange={handleChange} required />
                <FormInput label="Current Project" name="currentProjectName" value={formData.currentProjectName || ''} onChange={handleChange} />
            </fieldset>

            {/* --- User Account (Create Mode Only) --- */}
            {!isEditMode && (
                <fieldset className="grid grid-cols-12 gap-6">
                    <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">User Account</legend>
                    <FormInput label="User Email" name="user.email" type="email" value={formData.user?.email || ''} onChange={handleChange} required />
                    <FormInput label="Password" name="user.password" type="password" value={formData.user?.password || ''} onChange={handleChange} required />
                </fieldset>
            )}

            {/* --- Personal Details --- */}
            <fieldset className="grid grid-cols-12 gap-6">
                <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">Personal Details</legend>
                <FormInput label="Full Name" name="personalDetails.fullName" value={formData.personalDetails?.fullName || ''} onChange={handleChange} required />
                <FormInput label="Date of Birth" name="personalDetails.dateOfBirth" type="date" value={formData.personalDetails?.dateOfBirth || ''} onChange={handleChange} disabled={isEditMode} required />
                <div className="col-span-12 sm:col-span-6 md:col-span-4">
                    <label htmlFor="gender" className="block text-sm font-medium text-slate-700">Gender</label>
                    <select id="gender" name="personalDetails.gender" value={formData.personalDetails?.gender || ''} onChange={handleChange} disabled={isEditMode} required className="mt-1 block w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-purple-500 focus:border-purple-500 disabled:bg-slate-100">
                        <option value="">Select...</option><option value="Male">Male</option><option value="Female">Female</option><option value="Other">Other</option>
                    </select>
                </div>
                <FormInput label="Age" name="personalDetails.age" type="number" value={formData.personalDetails?.age || ''} onChange={handleChange} required />
                <FormInput label="Mobile" name="personalDetails.mobile" value={formData.personalDetails?.mobile || ''} onChange={handleChange} required pattern="\d{10}" title="10 digits" />
                <FormInput label="Personal Email" name="personalDetails.personalEmail" type="email" value={formData.personalDetails?.personalEmail || ''} onChange={handleChange} required />
                <FormInput label="Emergency Contact Name" name="personalDetails.emergencyContactName" value={formData.personalDetails?.emergencyContactName || ''} onChange={handleChange} required />
                <FormInput label="Emergency Contact Mobile" name="personalDetails.emergencyContactMobile" value={formData.personalDetails?.emergencyContactMobile || ''} onChange={handleChange} required pattern="\d{10}" title="10 digits" />
                <AddressFields legend="Current Address" namePrefix="personalDetails.currentAddress" data={formData.personalDetails?.currentAddress} onChange={handleChange} />
                <AddressFields legend="Permanent Address" namePrefix="personalDetails.permanentAddress" data={formData.personalDetails?.permanentAddress} onChange={handleChange} />
            </fieldset>

            {/* --- Professional Details --- */}
            <fieldset className="grid grid-cols-12 gap-6">
                <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">Professional Details</legend>
                <FormInput label="Employment Code" name="professionalDetails.employmentCode" value={formData.professionalDetails?.employmentCode || ''} onChange={handleChange} disabled={isEditMode} required pattern="\d{6}" title="Must be 6 digits" />
                <FormInput label="Company Email" name="professionalDetails.companyEmail" type="email" value={formData.professionalDetails?.companyEmail || ''} onChange={handleChange} disabled={isEditMode} required />
                <FormInput label="Date of Joining" name="professionalDetails.dateOfJoining" type="date" value={formData.professionalDetails?.dateOfJoining || ''} onChange={handleChange} disabled={isEditMode} required />
                <FormInput label="Office Phone" name="professionalDetails.officePhone" value={formData.professionalDetails?.officePhone || ''} onChange={handleChange} required pattern="\d{8,12}" title="8 to 12 digits" />
                <FormInput label="Reporting Manager Code" name="professionalDetails.reportingManagerEmployeeCode" value={formData.professionalDetails?.reportingManagerEmployeeCode || ''} onChange={handleChange} required />
                <FormInput label="HR Name" name="professionalDetails.hrName" value={formData.professionalDetails?.hrName || ''} onChange={handleChange} required />
                <AddressFields legend="Office Address" namePrefix="professionalDetails.officeAddress" data={formData.professionalDetails?.officeAddress} onChange={handleChange} />
            </fieldset>

            {/* --- Project Details --- */}
            <fieldset className="grid grid-cols-12 gap-6">
                <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">Project Details</legend>
                <FormInput label="Project Code" name="projects.0.projectCode" value={formData.projects?.[0]?.projectCode || ''} onChange={handleChange} />
                <FormInput label="Client/Project Name" name="projects.0.clientOrProjectName" value={formData.projects?.[0]?.clientOrProjectName || ''} onChange={handleChange} />
                <FormInput label="Start Date" name="projects.0.startDate" type="date" value={formData.projects?.[0]?.startDate || ''} onChange={handleChange} />
                <FormInput label="End Date" name="projects.0.endDate" type="date" value={formData.projects?.[0]?.endDate || ''} onChange={handleChange} />
                <FormInput label="Reporting Manager Code" name="projects.0.reportingManagerEmployeeCode" value={formData.projects?.[0]?.reportingManagerEmployeeCode || ''} onChange={handleChange} />
            </fieldset>

            {/* --- Finance Details --- */}
            <fieldset className="grid grid-cols-12 gap-6">
                <legend className="col-span-12 text-lg font-semibold text-purple-600 border-b pb-2 mb-2">Finance Details</legend>
                <FormInput label="PAN Card" name="finance.panCard" value={formData.finance?.panCard || ''} onChange={handleChange} required />
                <FormInput label="Aadhar Card" name="finance.aadharCard" value={formData.finance?.aadharCard || ''} onChange={handleChange} required />
                <FormInput label="CTC Breakup" name="finance.ctcBreakup" value={formData.finance?.ctcBreakup || ''} onChange={handleChange} required />
                <FormInput label="Bank Name" name="finance.bankDetails.bankName" value={formData.finance?.bankDetails?.bankName || ''} onChange={handleChange} required />
                <FormInput label="Branch" name="finance.bankDetails.branch" value={formData.finance?.bankDetails?.branch || ''} onChange={handleChange} required />
                <FormInput label="IFSC Code" name="finance.bankDetails.ifscCode" value={formData.finance?.bankDetails?.ifscCode || ''} onChange={handleChange} required />
            </fieldset>

            {error && <p className="text-red-500 text-sm text-center">{error}</p>}

            {/* --- Actions --- */}
            <div className="flex justify-end gap-4 pt-6 border-t sticky bottom-0 bg-white py-4">
                <button type="button" onClick={onClose} className="bg-white text-slate-700 px-6 py-2 rounded-lg font-semibold hover:bg-slate-100 border border-slate-300">
                    Cancel
                </button>
                <button type="submit" className="bg-purple-600 text-white px-6 py-2 rounded-lg font-semibold hover:bg-purple-700">
                    {isEditMode ? 'Save Changes' : 'Create Employee'}
                </button>
            </div>
        </form>
    );
};


export default EmployeeForm;